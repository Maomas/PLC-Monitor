package be.sam.application.Api_Monitoring;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Message;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;


import Simatic_S7.S7;
import Simatic_S7.S7Client;
import Simatic_S7.S7OrderCode;
import be.sam.application.R;

//Cette classe permet de récupérer le contenu des variables A.P.I.
public class ReadTabletsS7 {

    //Les constantes utilisées pour la gestion du traitement activé par les messages (Avant le traitement, Màj des résultats intermédiaires, Après le traitement)
    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;
    private static final int MESSAGE_BOTTLES_UPDATE = 4;
    private static final int MESSAGE_PILLS_UPDATE = 5;

    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private ProgressBar pbProgression;
    private TextView tvPlc;
    private Button btnConnection;
    private TextView tvBottles;
    private TextView tvPills;
    private TextView tvConnectionType;


    private AutomateS7 plcS7;
    private Thread readThread;

    private ColorStateList baseColor;

    //S7Client est nécessaire pour la connexion à l'API
    private S7Client comS7;
    private String[] param = new String[10];
    private byte[] bottlesPLC = new byte[2];
    private byte[] pillsPLC = new byte[2];
    private byte[] referencePLC = new byte[512];

    /*Le constructeur prend en paramètre plusieurs vues :
        - Le bouton permettant d'initier la connexion
        - La barre de progression qui se remplie si la connexion s'est correctement effectuée
        - Les textviews qui affichent les données lues depuis l'automate
    */
    public ReadTabletsS7(Button b, ProgressBar p, TextView t1, TextView t2, TextView t3, TextView t4){
        pbProgression = p;
        btnConnection = b;
        tvPlc = t1;
        tvBottles = t2;
        tvPills = t3;
        tvConnectionType = t4;

        comS7 = new S7Client();
        plcS7 = new AutomateS7();

        readThread = new Thread(plcS7);

        baseColor = tvPlc.getTextColors();
    }

    //Méthode appelée pour fermer la connexion à l'automate et interrompre le traitement de lecture
    public void Stop(){
        isRunning.set(false);
        comS7.Disconnect();
        readThread.interrupt();
    }

    //Méthode appelée pour initier une connexion à l'automate et démarrer le processus de lecture des données
    public void Start(String ipAddress, String rack, String slot){
        if(!readThread.isAlive()){
           param[0] = ipAddress;
           param[1] = rack;
           param[2] = slot;

           readThread.start();
        isRunning.set(true);
        }
    }

    private void downloadOnPreExecute(int t){
        //On affiche la référence de l'api dans la TextView
        tvPlc.setText("PLC Reference : "+ String.valueOf(t));
        tvPlc.setTextColor(Color.rgb(9,171,31));

    }

    private void downloadOnProgressUpdate(int progress){

        pbProgression.setProgress(progress);
    }

    private void downloadOnPillsUpdate(int pills){
        tvPills.setText("Pills number per bottle : "+Integer.toString(pills));
        tvPills.setTextColor(Color.rgb(9,171,31));
    }

    private void downloadOnBottlesUpdate(int bottles){
        tvBottles.setText("Bottles number : "+Integer.toString(bottles));
        tvBottles.setTextColor(Color.rgb(9,171,31));
    }

    //Réinitialise la barre de progression
    private void downloadOnPostExecute(){
        pbProgression.setProgress(0);
        //On affiche que la référence de l'API est indisponible
        tvPlc.setText("PLC reference : disconnected");
        tvConnectionType.setText("Connection type : disconnected");
        tvBottles.setText("Bottles number : disconnected");
        tvPills.setText("Pills number per bottle : disconnected");
        tvConnectionType.setTextColor(baseColor);
        tvPlc.setTextColor(baseColor);
        tvBottles.setTextColor(baseColor);
        tvPills.setTextColor(baseColor);


    }

    //Cette classe va permettre de gérer les 3 messages différentes envoyés au thread
    private Handler handler = new Handler(){
        /*
        La méthode surchargée handleMessage() permet de récupérer les différents messages
        envoyés au thread et d'exécuter la méthode nécessaire
        Elle prend en argument le message envoyé
        Le type de message est contenu dans la variable "what"
        En fonction du message, le traitement sera différent
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute(msg.arg1);
                    break;
                case MESSAGE_PROGRESS_UPDATE:
                    downloadOnProgressUpdate(msg.arg1);
                    break;
                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();
                    break;
                case MESSAGE_BOTTLES_UPDATE:
                    downloadOnBottlesUpdate(msg.arg1);
                    break;
                case MESSAGE_PILLS_UPDATE:
                    downloadOnPillsUpdate(msg.arg1);
                default:
                    break;
            }
        }
    };

    private class AutomateS7 implements Runnable{
        @Override
        public void run() {
            try{
                //La méthode SetConnectionType() indique le type de connexion
                comS7.SetConnectionType(S7.S7_BASIC);
                //ConnectTo() retourne 1 si la connexion a réussi
                Integer res = comS7.ConnectTo(param[0], Integer.valueOf(param[1]), Integer.valueOf(param[2]));
                S7OrderCode orderCode = new S7OrderCode();
                //GetOrderCode() retourne 1 si le traitement est réussi
                Integer result = comS7.GetOrderCode(orderCode);
                int numCPU=-1;
                if(res.equals(0) && result.equals(0)){
                    numCPU = Integer.valueOf(orderCode.Code().substring(5,8));
                }
                else numCPU = 0000;
                sendPreExecuteMessage(numCPU);

                //Traitement en boucle → tant que l'automate fonctionne
                while(isRunning.get()){
                    //Si on arrive à se connecter à l'automate
                    if(res.equals(0)){
                        int reference = comS7.ReadArea(S7.S7AreaDB, 5, 0, 20, referencePLC);
                        int bottles = comS7.ReadArea(S7.S7AreaDB, 5, 16, 2, bottlesPLC);
                        int pills = comS7.ReadArea(S7.S7AreaDB, 5, 15,2, pillsPLC);

                        int data=0;

                        if(reference == 0){
                            /*
                            GetWordAt() permet de lire la donnée
                            l'index de lecture dans le tableau est 0 → On lit la 1e variable
                             */
                            data = S7.GetWordAt(referencePLC, 0);
                            sendProgressMessage(data);
                        }

                        if(bottles == 0){
                            data = S7.GetWordAt(bottlesPLC, 0);
                            sendBottlesMessage(data);
                        }

                        if(pills == 0){
                            data = S7.BCDtoByte(pillsPLC[0]);
                            sendPillsMessage(data);
                        }
                    }
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                //On envoie un message lorsque le traitement est terminé
                sendPostExecuteMessage();
            }
            catch(Exception e){
                e.printStackTrace();
            }


        }

        //On définit le message via la constante après le traitement principal
        private void sendPostExecuteMessage(){
            Message msg = new Message();
            msg.what = MESSAGE_POST_EXECUTE;
            handler.sendMessage(msg);
        }

        /*
        On définit le message via la constante avant le traitement
        On transfert la référence de l'API dans le message
         */
        private void sendPreExecuteMessage(int v){
            Message msg = new Message();
            msg.what = MESSAGE_PRE_EXECUTE;
            msg.arg1 = v;
            handler.sendMessage(msg);
        }

        /*
        On définit le message pendant le traitement principal via la constante
        On transfert la valeur dans la variable récupérée dans l'API
         */
        private void sendProgressMessage(int i){
            Message msg = new Message();
            msg.what = MESSAGE_PROGRESS_UPDATE;
            msg.arg1 = i;
            handler.sendMessage(msg);
        }

        private void sendBottlesMessage(int bottles){
            Message msg = new Message();
            msg.what = MESSAGE_BOTTLES_UPDATE;
            msg.arg1 = bottles;
            handler.sendMessage(msg);
        }

        private void sendPillsMessage(int pills){
            Message msg = new Message();
            msg.what = MESSAGE_PILLS_UPDATE;
            msg.arg1 = pills;
            handler.sendMessage(msg);
        }
    }

}

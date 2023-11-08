import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {
    static ArrayList<Socket> sockets = new ArrayList<>();
    static ServerSocket server = null;
    static int[] arr = null;
    static int count =0;
    public static void main(String[] args) {
        try {
            server = new ServerSocket(50001);
            while(true) {
                System.out.println("대기");
                Socket socket = server.accept(); // 2명만 접속허용
                count++;


                new Thread(() -> {
                    if (count <= 2) {
                        sockets.add(socket);
                        arr = MakeArray();
                        game(socket);
                        count -= 2;
                    }
                }).start();


            }


        }catch (Exception e){

        }
    }
    static int i = 0;
    public static void game(Socket socket){
        DataInputStream dis = null;
        DataOutputStream dos = null;
        int m = 0;
        int[] nums = null;
        String id = "";
        InetSocketAddress isa = (InetSocketAddress)  socket.getRemoteSocketAddress();
        System.out.println("[서버] " + isa.getHostName() + "의 연결 요청을 수락함");
        try {
            dis = new DataInputStream(socket.getInputStream());
            id = dis.readUTF();
            String message = "";
            String numbs ="";

//            if (i==0) {
//                dos = new DataOutputStream(socket.getOutputStream());
//                dos.writeUTF("몇자리 숫자로 게임을 진행 하시겠습니까?");
//                dos.flush();
//            }
//            if(i == 0) {
//                m = dis.readInt();
//                i++;
//            }

            while (true) {
                int ball=0, strike=0;
                numbs = dis.readUTF();
                int numb[] = new int[m];
                for (int i = 0; i < m; i++) {
                    arr[i] = Integer.parseInt(String.valueOf(numbs.charAt(i)));
                }
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < m; j++) {
                        if (nums[i] == numb[j]) {
                            if (i == j)
                                strike++;
                            else ball++;
                        }
                    }
                }
                if (strike == 0 && ball == 0) {
                    message = numbs + " 는 Out입니다. ";
                    System.out.println("Out입니다. ");
                } else if(strike == m){
                     message = numbs + " 는 정답입니다!!!!";
                    System.out.println(message);
                }
                 else {
                    message = id + " : " + numbs + " " + ball + "B " + strike + "S ";
                    System.out.println(id + " : " + ball + "B " + strike + "S ");
                }
                for (Socket s : sockets) {
                    dos = new DataOutputStream(s.getOutputStream());
                    if(strike == m)
                        dos.writeUTF("End");
                    else dos.writeUTF("Able");
                    dos.writeUTF(message);
                    dos.flush();
                    if(strike == m) {
                        i = 0;
                    }
                }
            }
        }catch (Exception e){
            try {
                System.out.println(id + "님이 나가셨습니다. ");
                sockets.remove(socket);
                socket.close();
            }catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
    }
    public static int randomMake(){
        Random rd = new Random();
        return rd.nextInt(9);
    }
    static  int m;

    public static int[] MakeArray(){
        int[] nums ;
        m=4;
        nums = new int[m];
        for(int i = 0; i < m; i++){
            nums[i] = randomMake();
            for(int j=0; j<i; j++)
                if(nums[i] == nums[j])
                    i--;
        }
        for(int n : nums) {
            System.out.println(n);
        }
        return nums;
    }

}

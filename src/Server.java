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
            server = new ServerSocket(50002);
            while(true) {
                System.out.println("대기");
                Socket socket = server.accept(); // 2명만 접속허용
                new Thread(() -> {
                    if (count < 2) {
                        count++;
                        sockets.add(socket);
                        game(socket);
                        count --;
                    }
                }).start();

            }
        }catch (Exception e){}
    }
    static boolean check = true;
    static int in;
    static int m;

    public static void game(Socket socket){
        DataInputStream dis;
        DataOutputStream dos;
        String id = "";
        boolean checking;
        InetSocketAddress isa = (InetSocketAddress)  socket.getRemoteSocketAddress();
        System.out.println("[서버] " + isa.getHostName() + "의 연결 요청을 수락함");
        try {
            dis = new DataInputStream(socket.getInputStream());
            id = dis.readUTF();
            String message;
            String numbs;
            if(check){
                check = false;
                System.out.println("이니셜라이즈");
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("init");
                m = Integer.parseInt(dis.readUTF());
                in = Integer.parseInt(dis.readUTF());
                arr =makeArray(m);
                checking  = true;
            } else {
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("bb");
                checking = false;
            }
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBoolean(checking);
            while (true) {
                int ball=0, strike=0;
                numbs = dis.readUTF();
                int numb[] = new int[m];
                for (int i = 0; i < m; i++) {
                    numb[i] = Integer.parseInt(String.valueOf(numbs.charAt(i)));
                }
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < m; j++) {
                        if (arr[i] == numb[j]) {
                            if (i == j)
                                strike++;
                            else ball++;
                        }
                    }
                }
                if (strike == 0 && ball == 0) {
                    message = "[" + in + "이닝] " + id + " : " +numbs + " 는 Out입니다. ";
                    System.out.println(message);
                } else if(strike == m){
                     message = "[" + in + "이닝] " + id + " : " + numbs + " 는 정답입니다!!!!";
                    System.out.println(message);
                }
                 else {
                    message = "[" + in + "이닝] " + id + " : " + numbs + " " + ball + "B " + strike + "S ";
                    System.out.println(message);
                }

                in--;
                System.out.println(in);
                if(in < 0){
                    message = "모든 이닝을 소진하셨습니다...";
                }
                for (Socket s : sockets) {
                    dos = new DataOutputStream(s.getOutputStream());
                    if(strike == m || in < 0) {
                        dos.writeUTF("End");
                            check = true;

//                        String ck = dis.readUTF();
//                        if(ck.equals("n") || ck.equals("N")) {
//                            check = true;
//                            dos.writeUTF("End");
//                        } else {
//                            game(socket);
//                        }
                    } else dos.writeUTF("Able");
                    dos.writeUTF(message);
                    checking = !checking;
                    dos.writeBoolean(checking);
                    dos.flush();
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
    public static int[] makeArray(int m){
        int[] nums;
        nums = new int[m];
        for(int i = 0; i < m; i++){
            nums[i] = new Random().nextInt(9);
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

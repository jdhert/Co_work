

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Client {
    static DataOutputStream dos = null;
    static DataInputStream dis = null;


    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        Scanner sc = new Scanner(System.in);
        String s1, s2;
        try {
            socket.connect(new InetSocketAddress("192.168.219.104", 50002));
            System.out.println("연결 시도중 ... 포트번호 : " + socket.getLocalPort());
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            System.out.print("사용자 아이디를 만들어 주새요 : ");
            dos.writeUTF(sc.nextLine());


            if (dis.readUTF().equals("init")) {
                System.out.print("몇자리 야구게임을 하시겠습니까? (3or4): ");
                s1 = sc.nextLine();
                System.out.print("몇 이닝까지 게임 하시겠습니까? : ");
                s2 = sc.nextLine();
                dos.writeUTF(s1);
                dos.writeUTF(s2);
            }

            //서버로부터 데이터를 읽는 로직
            new Thread(() -> {
                try {
                    while (true) {
                        String msq = dis.readUTF();
                        if(msq.equals("End")) {
                            System.out.println("게임이 종료되었습니다....");
                            return;
                        }
                        String meseeage = dis.readUTF();
                        System.out.println(meseeage);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            String msq = "";
            while (true) {
                System.out.print("숫자를 입력하세요 : ");
                dos.writeUTF(sc.nextLine());
                dos.flush();
            }
        }catch (IOException e){
            throw new RuntimeException();
        } finally {
            try {
                dos.close();
                socket.close();
            }catch (IOException e){
                throw new RuntimeException();
            }
        }
    }
}
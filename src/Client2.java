

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
    static DataOutputStream dos = null;
    static DataInputStream dis = null;
    static boolean checking;
    public static void main(String[] args) {
        Socket socket = new Socket();
        Scanner sc = new Scanner(System.in);
        String s1, s2;
        try {
            socket.connect(new InetSocketAddress("192.168.0.201", 50001));
            System.out.println("연결 시도중 ... 포트번호 : " + socket.getLocalPort());
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            while(true) {
                if (dis.readUTF().equals("NotFull")) {
                    System.out.print("사용자 아이디를 만들어 주새요 : ");
                    dos.writeUTF(sc.nextLine());
                    break;
                } else System.out.println("대기 바람... ");
            }

            if (dis.readUTF().equals("init")) {
                System.out.print("몇자리 야구게임을 하시겠습니까? (3or4): ");
                s1 = sc.nextLine();
                System.out.print("몇 이닝까지 게임 하시겠습니까? : ");
                s2 = sc.nextLine();
                dos.writeUTF(s1);
                dos.writeUTF(s2);
                dos.flush();
            } else System.out.println("상대가 설정중입니다... ");
            checking = dis.readBoolean();
            while (true) {
                if(checking) {
                    System.out.print("숫자를 입력하세요 : ");
                    dos.writeUTF(sc.nextLine());
                    dos.flush();
                } else System.out.println("상대가 입력할때 가지 대기하세요... ");
                String msq = dis.readUTF();
                if (msq.equals("End")){
                    System.out.println("게임이 종료되었습니다....");
                    return;
//                    System.out.println("다시 하시겠습니까? Y/N ");
//                    String s = sc.nextLine();
//                    dos.writeUTF(s);
//                    dos.flush();
//                    String s23 = dis.readUTF();
//                    if(s23.equals("End")) {
//                        System.out.println("게임이 종료되었습니다....");
//                        return;
//                    }
                } else if (msq.equals("Able")) {
                    String meseeage = dis.readUTF();
                    checking = dis.readBoolean();
                    System.out.println(meseeage);
                }
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
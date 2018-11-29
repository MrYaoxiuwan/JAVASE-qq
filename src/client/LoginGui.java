package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;


import Connect.Message;
import user.User;

public class LoginGui extends JFrame
{
	private Socket client ;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	//�������
	private JLabel N_jp;
	//�в����
	private JPanel M_jp;
	private JButton M_jb;
	private JTextField M_jt;
	private JPasswordField M_jpf;
	private JLabel M_jl1,M_jl2,M_jl3,M_jl4;
	private JCheckBox M_jc1,M_jc2;
	private LoginActionListener loginAction;

	//�Ϸ����
	JPanel S_jp;
	JButton S_jb1, S_jb2,S_jb3;
	
	public LoginGui(String title) throws HeadlessException 
	{
		super(title);
		/*
	 	 *���ӷ�������������Ӧ��ͨѶ���������
		 */
		try {
			this.client = new Socket("172.19.22.70",8888);
			out = new ObjectOutputStream(client.getOutputStream());
			in= new ObjectInputStream(client.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
	}
	/*
	 * ���ý��桢����Ĭ�ϱ߽��Ʋ��֡�����Ĭ�����Ʋ���
	 */
	public void init() 
	{
		
		loginAction = new LoginActionListener();
		//���ñ����������
		N_jp = new JLabel(new ImageIcon("image/tou.gif"));
		
		//�����в����
		M_jp = new JPanel(null);
		M_jl1 = new JLabel("QQ�˺�");
		M_jl1.setBounds(80, 15, 80, 20);
		
		M_jl2 = new JLabel("QQ����");
		M_jl2.setBounds(80, 55, 80, 20);
		M_jl3 = new JLabel("��������");
		M_jt = new JTextField("123");
		M_jt.setBounds(160, 15, 120, 25);
		
		M_jpf = new JPasswordField("123");
		M_jpf.setBounds(160, 55, 120, 25);
		M_jb = new JButton(new ImageIcon("image/clear.gif"));
		S_jb1 = new JButton(new ImageIcon("image/denglu.gif"));
		S_jb1.addActionListener(loginAction);
		
		S_jb1.setBounds(80, 100, 65, 20);
		
		S_jb2 = new JButton(new ImageIcon("image/quxiao.gif"));
		S_jb3 = new JButton(new ImageIcon("image/xiangdao.gif"));
		S_jb3.addActionListener(loginAction);
		S_jb3.setBounds(160, 100, 65, 20);
		
		M_jp.add(M_jl1);
		M_jp.add(M_jt);
		M_jp.add(M_jl2);
		M_jp.add(M_jpf);
		M_jp.add(S_jb1);
		M_jp.add(S_jb3);
		
		//�������������ڱ���
		this.add(N_jp,"North");
		
		//�����������в�
		this.add(M_jp);
		
		
		
		this.setLocation(600,250);
		this.setIconImage(Toolkit.getDefaultToolkit ().getImage ("image/qq.gif"));
		this.setSize(350, 220);
		this.setResizable(false);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.paintComponents(getGraphics());
	}
	
	public static void main(String[] args) 
	{
		new	LoginGui("QQ");
	}
	
	class LoginActionListener implements ActionListener 
	{
		@Override
		/*
		 * ��ť������Ӧ�¼�
		 */
		public void actionPerformed(ActionEvent e) 
		{
			/*
			 * �ж��¼�Դ
			 */
			if(e.getSource()==S_jb1) 
			{
					//������Ҫ�жϵ��˺���Ϣ
					User u = new User();
					u.setId(M_jt.getText());
					u.setPassword(M_jpf.getText());
					
					//���˺���Ϣ����Ϣ���ͷ�װΪ��Ϣ��
					Message fsMes = new Message();
					fsMes.setFrom(u);
					fsMes.setType("login");
					
					//����Ϣ�����������
					try {
						out.writeObject(fsMes);
						out.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					//���շ��������صĽ�����Բ�ͬ�������ͬ����
					try {
						Message loginResult=(Message)in.readObject();
						if(loginResult.getFrom()!=null) {
							FunctionGui  m=new FunctionGui(loginResult.getFrom(),loginResult.getFrendList(),out,in);
	//						m.setVisible(true);
							LoginGui.this.dispose();
						}else
						{
							JOptionPane.showMessageDialog(LoginGui.this, "�˺Ż��������", "������ʾ", JOptionPane.ERROR_MESSAGE);
						}
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}else if(e.getSource()==S_jb3) 
			{
				//ע����Ϣ����
				RegisterGui  r=new RegisterGui(out,in,LoginGui.this);
//				r.setVisible(true);
				//����½�������أ���ע��ɹ�����Ҫ���ص�½ʱ��ʾ
				LoginGui.this.setVisible(false);
//				LoginGui.this.dispose();
			}
		}
		
	}

}

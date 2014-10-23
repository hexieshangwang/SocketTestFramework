package socket.io.ui;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import socket.io.SocketIO;
import socket.io.config.ResourceManager;

public class GUI extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private static JTextArea inputTextArea, outputTextArea;
	private JButton requestButton, clearButton;
	private JPanel inputPanel, outputPanel, buttonPanel;
	private JLabel inputLabel, outputLabel;
	private JScrollPane inputScrollPane, outputScrollPane;
	private SocketIO io;

	public GUI(String title) {
		super(title);
		this.setLayout(new BorderLayout());

		inputLabel = new JLabel("Input");
		outputLabel = new JLabel("Output");
		requestButton = new JButton("Send");
		clearButton = new JButton("Clear");
		inputTextArea = new JTextArea(14,100);
		outputTextArea = new JTextArea(14,100);
		outputTextArea.setBackground(Color.BLACK);
		outputTextArea.setForeground(Color.WHITE);
		inputTextArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		outputTextArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		inputScrollPane = new JScrollPane(inputTextArea);
		outputScrollPane = new JScrollPane(outputTextArea);
		inputPanel = new JPanel();
		buttonPanel = new JPanel();
		outputPanel = new JPanel();

		
		this.add(inputPanel,BorderLayout.NORTH);
		this.add(buttonPanel,BorderLayout.CENTER);
		this.add(outputPanel,BorderLayout.SOUTH);
		
		inputPanel.add(inputLabel);
		inputPanel.add(inputScrollPane);
		inputPanel.setLayout(new FlowLayout());
		
		buttonPanel.add(requestButton);
		buttonPanel.add(clearButton);
		buttonPanel.setLayout(new FlowLayout());
		
		outputPanel.add(outputLabel);
		outputPanel.add(outputScrollPane);
		outputPanel.setLayout(new FlowLayout());
		
		requestButton.addActionListener(this);
		clearButton.addActionListener(this);

		this.setExtendedState(MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.validate();
		
		io = new SocketIO(ResourceManager.getInstance()
				.getServerHost(), ResourceManager.getInstance()
				.getServerPort());
	}

	// Text Output
	public static void printOutputTextArea(String text) {
		outputTextArea.setCaretPosition(outputTextArea.getText().length());
		outputTextArea.append(text);
		outputTextArea.append("\n");
	}
	
	public static void printInputTextArea(String text){
		inputTextArea.setText(text);
	}

	public static void main(String[] args) {
		new GUI("Socket Test Tool v1.0");
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == requestButton){
			io.send(inputTextArea.getText().trim());
		}else if(e.getSource() == clearButton){
			inputTextArea.setText("");
		}
		
	}
}

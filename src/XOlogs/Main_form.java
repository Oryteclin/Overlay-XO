package XOlogs;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

public class Main_form {

	private JFrame frame;
	private JTextField textField;
	public static LogReader myLR;
	public static LogReader myLR2;
	public ArrayList<Player> PL;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Main_form window = new Main_form();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main_form() {
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(500, 650));
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 1176, 692);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{1160, 0};
		gbl_panel_2.rowHeights = new int[]{81, 576, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTH;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel_2.add(panel_1, gbc_panel_1);
		panel_1.setLayout(null);
		
		JLabel lblLogsPath = new JLabel("Log's path:");
		lblLogsPath.setBounds(34, 9, 82, 14);
		panel_1.add(lblLogsPath);
		
		textField = new JTextField();
		textField.setBounds(126, 6, 490, 20);
		panel_1.add(textField);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setColumns(10);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(34, 45, 82, 23);
		panel_1.add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(126, 45, 96, 23);
		panel_1.add(btnStop);
		

		
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(e -> {
			final JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory()+"\\My games\\Crossout\\logs");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("combat.log", "log");
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(filter);
			int returnValue = fc.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				textField.setText(fc.getSelectedFile().getAbsolutePath());

			}

		});
		btnOpen.setBounds(626, 5, 89, 23);
		panel_1.add(btnOpen);
		

		btnStop.addActionListener(e -> {
			if ((myLR != null) ) {
				myLR.interrupt();}

			if ((myLR2 != null) ) {
				myLR2.interrupt();}
		});
		
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		panel_2.add(panel, gbc_panel);
		panel.setMinimumSize(new Dimension(50, 50));
		panel.setAutoscrolls(true);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 1140, 554);
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		btnStart.addActionListener(e -> {

			if (((myLR == null)) || (myLR.getState().equals(Thread.State.TERMINATED))) {
				if (!(myLR == null)){
					System.out.print(myLR.getState());}
				PL = new ArrayList();


				myLR = new LogReader();
				myLR.SetPlayers(PL);
				myLR.SetPath(textField.getText());


				myLR.setTextArea(textArea);

				File F = new File(textField.getText());


				myLR2 = new LogReader();
				myLR2.SetType(1);
				myLR2.setTextArea(textArea);
				myLR2.SetPath(F.getParent() +"//game.log");
				myLR2.SetPlayers(PL);

				myLR.SetMyName(myLR.readMyName());
				myLR2.SetMyName(myLR.getMyName());
				myLR.start();
				myLR2.start();
			}


		});
	}
}

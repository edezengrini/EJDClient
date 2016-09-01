package client.dialog.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ReportException {

	private static String[] options = new String[] { "Ok", "Detalhes" };
	private static Dimension minimizeSize = null;

	public static void report(Component component, Exception e) {

		e.printStackTrace();

		int option = JOptionPane.showOptionDialog(component, buscarMensagem(e), "Mensagem", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.NO_OPTION)
			reportDetail(e);
	}

	private static String buscarMensagem(Exception e) {
		if (e == null || e.getMessage() == null) {
			return "Erro Interno.";
		}

		Throwable ex = e;
		
		while(ex.getCause() != null){
			ex = ex.getCause();
			if(ex instanceof SQLException){
				return ex.getMessage();
			}
		}
		
		return e.getMessage();
	}

	public static void reportDetail(Exception ex) {
		final JTextArea textArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(textArea);
		textArea.setEditable(false);

		JButton btnOk = new JButton("Ok");
		btnOk.setMnemonic('O');
		JButton btnMax = new JButton("Maximizar");
		btnMax.setMnemonic('M');

		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = (JDialog) ((JComponent) e.getSource()).getRootPane().getParent();
				dialog.dispose();
			}
		});

		btnMax.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();

				JDialog dialog = (JDialog) btn.getRootPane().getParent();

				if (minimizeSize == null)
					minimizeSize = dialog.getSize();

				if (btn.getText().equals("Maximizar")) {
					btn.setText("Minimizar");
					dialog.setSize(UtilClient.getScreenSize());
					dialog.setLocationRelativeTo(null);
				} else {
					btn.setText("Maximizar");
					dialog.setSize(minimizeSize);
					dialog.setLocationRelativeTo(null);
				}

				dialog.setResizable(true);

			}
		});

		Object[] options = new Object[] { btnOk, btnMax };

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ex.printStackTrace(new PrintStream(baos));
		textArea.append(baos.toString());

		scroll.setPreferredSize(new Dimension(500, 200));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		textArea.setCaretPosition(0);

		JOptionPane.showOptionDialog(null, scroll, "Mensagem", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

	}

	public static void main(String[] args) {
		report(null, new Exception("teste",new SQLException("sqlteste")));
	}
}

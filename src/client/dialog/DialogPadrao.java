
package client.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public abstract class DialogPadrao extends JDialog{

	private static final long serialVersionUID = 1L;

	protected final JButton btnSair = new JButton("Sair");
	protected final JPanel panelComando = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0)){
		private static final long serialVersionUID = 1L;

		public java.awt.Component add(java.awt.Component comp) {
			super.add(comp,0);
			if(comp instanceof JButton){
				JButton button = (JButton) comp;
				button.setMargin(new Insets(2, 4, 2, 4));
				button.setFocusable(false);
			}
			return comp;
		};
	};

	protected final JPanel panelCenter = new JPanel();
	protected final JPanel panelPrincipal = new JPanel();

	public DialogPadrao(Window window) {
		super(window);
		setModal(true);

		setModalityType(ModalityType.MODELESS);
		setTitle(getTitulo());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);


		getContentPane().add(this.panelCenter, BorderLayout.CENTER);
		this.panelCenter.setLayout(new BorderLayout(0, 0));

		this.panelComando.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		getContentPane().add(this.panelComando, BorderLayout.NORTH);

		this.panelComando.add(this.btnSair);
		this.panelCenter.add(this.panelPrincipal, BorderLayout.CENTER);
		this.panelPrincipal.setLayout(new BorderLayout(0, 0));

		this.btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		this.btnSair.setMnemonic('S');
		this.setFocusableWindowState(true);

	}

	public abstract String getTitulo();



}

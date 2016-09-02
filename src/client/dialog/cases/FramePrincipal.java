package client.dialog.cases;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import server.configurador.Configurador;
import server.dao.ProdutoDAO;
import server.persistence.Persistence;
import arquitetura.client.components.dialog.EJDOptionPane;
import arquitetura.client.runner.EJDThread;
import arquitetura.client.runner.EJDThread.DoInBackground;
import client.dialog.util.ReportException;
import client.dialog.util.UtilClient;
import client.enums.EnumImage;

public class FramePrincipal extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private final JMenuBar menuBar = new JMenuBar();

	private final JMenu menuCadastros = new JMenu("Cadastros");
	private final JMenuItem menuItemProduto = new JMenuItem("Produto");
	private final JMenuItem menuItemGrupo = new JMenuItem("Grupo");
    private final JMenuItem menuItemPessoaFisica = new JMenuItem("Pessoa Física");

	private final JMenu menuMovimentos = new JMenu("Movimentos");
	private final JMenuItem menuItemCompra = new JMenuItem("Entrada");
	private final JMenuItem menuItemVenda = new JMenuItem("Saída");
	private final JMenuItem menuItemEstoque = new JMenuItem("Estoque");

	private final JMenu menuOutros = new JMenu("Outros");
	private final JMenuItem menuItemLimparBase = new JMenuItem("Limpar Base");
	private final JMenuItem menuItemLog = new JMenuItem("Log");
	private final JMenuItem menuConfigurar = new JMenuItem("Configurar");

	private final JMenuItem menuItemUsuario = new JMenuItem("Usuario");

	private static FramePrincipal frame = new FramePrincipal();

//	public static void main(String[] args) {
//		new DialogLogin().setVisible(true);
//	}

    public static void main(String[] args) {

//        try {
//            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (Exception ignore) {
//
//        }

        Persistence.getInstance();

        frame = new FramePrincipal();

        frame.setVisible(true);

    }

	public FramePrincipal() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(FramePrincipal.class.getResource(EnumImage.ICONE.getCaminho())));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Sistema EJD");

		setMinimumSize(new Dimension(800,600));
		setLocationRelativeTo(null);
//		setExtendedState(Frame.MAXIMIZED_BOTH);

		setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);

		setJMenuBar(this.menuBar);
		this.menuCadastros.setMnemonic('C');

		this.menuBar.add(this.menuCadastros);
		this.menuItemProduto.setMnemonic('P');
		this.menuItemProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				frame.setEnabled(false);
				new DialogProdutoProducao(FramePrincipal.this).setVisible(true);
//				frame.setEnabled(true);
			}
		});

        this.menuItemGrupo.setMnemonic('G');
        this.menuItemGrupo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DialogGrupo(FramePrincipal.this).setVisible(true);
            }
        });

        this.menuItemPessoaFisica.setMnemonic('F');
        this.menuItemPessoaFisica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DialogPessoaFisica(FramePrincipal.this).setVisible(true);
            }
        });

		this.menuCadastros.add(this.menuItemProduto);
		this.menuCadastros.addSeparator();
		this.menuCadastros.add(this.menuItemGrupo);
		this.menuCadastros.add(this.menuItemPessoaFisica);

		this.menuMovimentos.setMnemonic('M');

		this.menuBar.add(this.menuMovimentos);
		this.menuItemCompra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				frame.setEnabled(false);
				new DialogEntrada(FramePrincipal.this).setVisible(true);
//				frame.setEnabled(true);
			}
		});
		this.menuItemCompra.setMnemonic('C');

		this.menuMovimentos.add(this.menuItemCompra);
		this.menuItemVenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				frame.setEnabled(false);
				new DialogSaida(FramePrincipal.this).setVisible(true);
//				frame.setEnabled(true);
			}
		});
		this.menuItemVenda.setMnemonic('V');

		this.menuMovimentos.add(this.menuItemVenda);
		this.menuItemEstoque.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				frame.setEnabled(false);
				new DialogEstoque(FramePrincipal.this).setVisible(true);
//				frame.setEnabled(true);
			}
		});
		this.menuItemEstoque.setMnemonic('E');

		this.menuMovimentos.add(this.menuItemEstoque);
		this.menuOutros.setMnemonic('O');

		this.menuBar.add(this.menuOutros);
		this.menuItemLimparBase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excliurTudo();
			}
		});
		this.menuItemLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				frame.setEnabled(false);
				new DialogLogSql(FramePrincipal.this).setVisible(true);
//				frame.setEnabled(true);
			}
		});
		this.menuItemLog.setMnemonic('L');

		this.menuOutros.add(this.menuItemLog);
		this.menuItemLimparBase.setMnemonic('L');

		this.menuOutros.add(this.menuItemLimparBase);

		this.menuConfigurar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				    setCursor(UtilClient.CURSOR_WAIT);
					configurar();
					setCursor(UtilClient.CURSOR_DEFAULT);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		this.menuConfigurar.setMnemonic('C');

		this.menuOutros.add(this.menuConfigurar);

        menuCadastros.add(menuItemProduto);
        menuItemUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new DialogUsuario(FramePrincipal.this).setVisible(true);
            }
        });
        menuCadastros.add(menuItemUsuario);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Persistence.closeAll();
			}
		});


	}

	protected void configurar() {
        try {
            EJDThread.doJob(new DoInBackground() {

                @Override
                public void doInBackground() throws Exception {
                    new Configurador().configurar();
                    EJDOptionPane.informMessage("Configuração do Sistema realizada com sucesso.");
                }
            }, "Gerando Dados");

        } catch (Exception e){
            EJDOptionPane.informMessage("Problemas ao acessar os dados!\n" + e);
            e.printStackTrace();
        } finally {

        }
    }


    private void excliurTudo(){
		try{
			new ProdutoDAO().excluirTudo();
			JOptionPane.showMessageDialog(this, "Registros excluídos com sucesso!");
		}catch (Exception e) {
			ReportException.report(this,e);
		}
	}

}

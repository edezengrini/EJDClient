
package client.dialog;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import server.entities.abstracts.AbstractEntity;
import client.table.TableModelEntidade;

public class DialogLocalizar<E extends AbstractEntity> extends JDialog{

	private static final long serialVersionUID = 1L;

	private E entidadeSelecionada = null;
	private List<E> listaEntidades;
	private JTable tabela = new JTable();
	private JScrollPane scrollPane = new JScrollPane(tabela);
	private JPanel panelDados = new JPanel();
	private JLabel labelPesquisa = new JLabel("Pesquisa: ");
	private JTextField fieldPesquisa = new JTextField();
	private DefaultTableModel model = new DefaultTableModel();

	public DialogLocalizar(DialogCrud<E> dialog, List<E> listaEntidades) {
		super(dialog, true);
		this.listaEntidades = listaEntidades;
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setTitle("Localizando "+dialog.getTitle());
		setSize(dialog.getSize());

		setLocationRelativeTo(dialog);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		tabela.setAutoCreateRowSorter(true);

		panelDados.setLayout(new BorderLayout());
		getContentPane().add(panelDados, BorderLayout.NORTH);

		panelDados.add(labelPesquisa, BorderLayout.WEST);
		fieldPesquisa.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyReleased(KeyEvent e) {
//		        keyReleased(e);
		    }
		});
		panelDados.add(fieldPesquisa, BorderLayout.CENTER);

		tabela.setModel(model);
		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if(e.getClickCount() > 1){
					int selecionada = tabela.getSelectedRow();
					if(selecionada != -1){
						selecionada = tabela.getRowSorter().convertRowIndexToModel(selecionada);
						entidadeSelecionada = DialogLocalizar.this.listaEntidades.get(selecionada);
						dispose();
					}
				}
			}
		});

		tabela.setModel(new TableModelEntidade<E>(dialog.getTableModelEntidade()));
	}

	public E getEntidadeSelecionada() {
		return entidadeSelecionada;
	}

	public void keyReleased(KeyEvent evt) {

        try {
            System.out.println(evt.getKeyChar());
            Class<E> entidade = (Class<E>) entidadeSelecionada.getClass();
            listarEventos(entidade, fieldPesquisa.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


	protected void listarEventos(Class<E> entidade, String prestador) {
	    new Thread() {

	            }.start();
	           //dao.abrirTransacao();
//	            entidade = dao.getClasseEntidade();  // aqui é onde acontece a sua consulta é feita todas as vezes

	           // dao.fecharTransacao();
//	            model = new TbPrestadorServico(reserva);
	          // JTableHeader cabecalho = table.getTableHeader();
	            //cabecalho.setBackground(Color.gray);
	          //  cabecalho.setForeground(Color.yellow);

	            tabela.setModel(model);
//	            tabela.setAutoResizeMode(tabela.AUTO_RESIZE_OFF);
//	            tabela.getColumnModel().getColumn(0).setPreferredWidth(200);
//	            tabela.getColumnModel().getColumn(1).setPreferredWidth(600);
//	            tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
//	            tabela.getColumnModel().getColumn(3).setPreferredWidth(200);

	           /*
	           for(int i=0;i<reserva.size();i++){
	               System.out.println("TEste"+reserva.get(i).getNomeEmpresa());
	               System.out.println("TEste"+reserva.get(i).getPrestadorServico());
	               System.out.println("TEste"+reserva.get(i).getMatriculaPrestadorServico());
	               System.out.println("TEste"+reserva.get(i).getRgPrestadorServico());



	           }
	            */
	        }

}

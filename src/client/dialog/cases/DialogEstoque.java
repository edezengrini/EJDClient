
package client.dialog.cases;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import server.dao.EstoqueDAO;
import server.entities.Estoque;
import server.persistence.Persistence;
import client.dialog.DialogPadrao;
import client.table.TableModelEntidade;
import client.table.TableModelEntidade.ITableModelEntidade;

public class DialogEstoque extends DialogPadrao{
	private static final long serialVersionUID = 1L;

	private JTable tabela = new JTable();
	private JScrollPane scrollPane = new JScrollPane(tabela);
	private JButton buttonAtualizar = new JButton("Atualizar");
	
	private EstoqueDAO estoqueDAO = new EstoqueDAO();
	
	public DialogEstoque(Window window) {
		super(window);
		initComponents();
	}
	
	private void initComponents() {
		panelComando.add(buttonAtualizar);
		buttonAtualizar.setMnemonic('A');
		panelPrincipal.add(scrollPane);
		
		buttonAtualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				atualizarEstoque();
			}
		});
		
		atualizarEstoque();
	}		

	private void atualizarEstoque(){
		tabela.setModel(new TableModelEntidade<Estoque>(new ITableModelEntidade<Estoque>() {

			@Override
			public Object getCampoEntidade(Estoque entidade, int coluna) {
				switch (coluna) {
					case 0:
						return entidade.getIdEstoque();
					case 1:
						return entidade.getProduto().getDescricao();
					case 2:
						return entidade.getSaldo();
				}
				return null;
			}

			@Override
			public List<Estoque> getEntidades() {
				return estoqueDAO.localizarTodos(true);
			}

			@Override
			public String[] getNomesColunas() {
				return new String[]{"Identificador", "Produto" , "Saldo"};
			}
		}));
	}
	
	public static void main(String[] args) {
		
		try{
			new DialogEstoque(null).setVisible(true);
		}finally{
			Persistence.closeAll();
			System.exit(0);
		}		
	}


	@Override
	public String getTitulo() {
		return "Estoque";
	}
	
}


package client.dialog.cases;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import server.dao.LogSqlDAO;
import server.entities.LogSql;
import server.persistence.Persistence;
import client.dialog.DialogPadrao;
import client.table.TableModelEntidade;
import client.table.TableModelEntidade.ITableModelEntidade;

public class DialogLogSql extends DialogPadrao{
	private static final long serialVersionUID = 1L;

	private JTable tabela = new JTable();
	private JScrollPane scrollPane = new JScrollPane(tabela);
	private JButton btnAtualizar = new JButton("Atualizar");
	
	private LogSqlDAO logSqlDAO = new LogSqlDAO();
	
	public DialogLogSql(Window window) {
		super(window);
		initComponents();
	}
	
	private void initComponents() {
		panelComando.add(btnAtualizar);
		btnAtualizar.setMnemonic('A');
		panelPrincipal.add(scrollPane);
		
		btnAtualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				atualizarLogSql();
			}
		});
		
		atualizarLogSql();
	}		

	private void atualizarLogSql(){
		tabela.setModel(new TableModelEntidade<LogSql>(new ITableModelEntidade<LogSql>() {

			@Override
			public Object getCampoEntidade(LogSql entidade, int coluna) {
				switch (coluna) {
					case 0:
						return entidade.getIdLogSql();
					case 1:
						return entidade.getUsuario();
					case 2:
						return entidade.getOperacao();
					case 3:
						return entidade.getTabela();
					case 4:
						return entidade.getValores();
					case 5:
						return entidade.getDataExecucao();
				}
				return null;
			}

			@Override
			public List<LogSql> getEntidades() {
				return logSqlDAO.localizarTodos(true);
			}

			@Override
			public String[] getNomesColunas() {
				return new String[]{"Identificador", "Usuário" , "Operação","Tabela","Valores","Data da Execução"};
			}
		}));
	}
	
	public static void main(String[] args) {
		
		try{
			new DialogLogSql(null).setVisible(true);
		}finally{
			Persistence.closeAll();
			System.exit(0);
		}		
	}


	@Override
	public String getTitulo() {
		return "Log";
	}
	
}


package client.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import server.entities.interfaces.IEntity;

public class TableModelEntidade<E extends IEntity> extends AbstractTableModel{
	private static final long serialVersionUID = 1L;

	private ITableModelEntidade<E> tableModelEntidade;

	private List<E> listaEntidades;
	private String[] nomesColunas;
	private Map<Integer, Class<?>> colunasClass = new HashMap<Integer, Class<?>>();

	public TableModelEntidade(ITableModelEntidade<E> tableModelEntidade) {
		super();
		this.tableModelEntidade = tableModelEntidade;
		listaEntidades = tableModelEntidade.getEntidades() != null ? tableModelEntidade.getEntidades() : new ArrayList<E>();
		nomesColunas = tableModelEntidade.getNomesColunas() != null ? tableModelEntidade.getNomesColunas() : new String[0];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?> classe = colunasClass.get(colunasClass);
		if(classe == null){
			classe = buscarClasse(columnIndex);
			colunasClass.put(columnIndex, classe);
		}
		return super.getColumnClass(columnIndex);
	}

	private Class<?> buscarClasse(int coluna){
		for (int linha = 0; linha < getRowCount(); linha++) {
			Object valor = getValueAt(linha, coluna);
			if(valor != null){
				return valor.getClass();
			}
		}
		return null;
	}

	@Override
	public int getRowCount() {
		return listaEntidades.size();
	}

	@Override
	public int getColumnCount() {
		return nomesColunas.length;
	}

	@Override
	public String getColumnName(int column) {
		return nomesColunas[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		E entidade = listaEntidades.get(rowIndex);

		return tableModelEntidade.getCampoEntidade(entidade,columnIndex);
	}

	public interface ITableModelEntidade<E extends IEntity> {
		Object getCampoEntidade(E entidade, int coluna);
		List<E> getEntidades();
		String[] getNomesColunas();
	}

}

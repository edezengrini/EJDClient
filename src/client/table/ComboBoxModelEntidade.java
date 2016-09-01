
package client.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import server.entities.interfaces.IEntity;

public class ComboBoxModelEntidade<E extends IEntity> extends AbstractListModel implements ComboBoxModel{

	private static final long serialVersionUID = 1L;

	private E entidadeSelecionada = null;
	
	private List<E> listaEntidades;
	
	public ComboBoxModelEntidade(IComboBoxEntidade<E> comboBoxEntidade) {
		super();
		listaEntidades = comboBoxEntidade.getEntidades() != null ? comboBoxEntidade.getEntidades() : new ArrayList<E>();
	}

	@Override
	public int getSize() {
		return listaEntidades.size();
	}

	@Override
	public Object getElementAt(int indice) {
		return listaEntidades.get(indice);
	}
	
	public E getEntidadeSelecionada(int indice){
		return listaEntidades.get(indice);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void setSelectedItem(Object anItem) {
		entidadeSelecionada = (E) anItem;			
	}

	@Override
	public E getSelectedItem() {
		return entidadeSelecionada;
	}	

	public interface IComboBoxEntidade<E extends IEntity>{
		List<E> getEntidades();
	}
}

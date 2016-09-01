package client.dialog.cases;

import java.awt.BorderLayout;
import java.awt.Window;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import server.dao.AbstractDAO;
import server.dao.UnidadeMedidaDAO;
import server.entities.UnidadeMedida;
import server.entities.abstracts.EntityState;
import server.persistence.Persistence;
import arquitetura.client.components.panel.EJDCheckBox;
import arquitetura.client.components.textfield.EJDTextField;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.util.ValidateEntity;
import client.dialog.DialogCrud;
import client.table.TableModelEntidade.ITableModelEntidade;

public class DialogUnidadeMedida extends DialogCrud<UnidadeMedida> {
	private static final long serialVersionUID = 1L;

	private final JLabel lbIdentificador = new JLabel("Identificador:");
	private final JLabel lbDescricao = new JLabel("Descri\u00E7\u00E3o:");
	private final JLabel lbSigla = new JLabel("Sigla:");
	private final JTextField fieldIdentificador = new JTextField();
	private final EJDTextField fieldDescricao = new EJDTextField();
	private final EJDTextField fieldSigla = new EJDTextField();
	private final JPanel panel = new JPanel();
	private final EJDCheckBox chboxStatus = new EJDCheckBox("Ativo");

	public DialogUnidadeMedida(Window window) {
		super(window);
		initComponents();
	}

	private void initComponents() {
		panelPrincipal.setLayout(new MigLayout("", "[][grow]", "[][][]"));

		panelPrincipal.add(this.lbIdentificador, "cell 0 0,alignx trailing");

		panelPrincipal.add(this.fieldIdentificador, "cell 1 0,growx");

		panelPrincipal.add(this.lbDescricao, "cell 0 1,alignx trailing");

		panelPrincipal.add(this.fieldDescricao, "cell 1 1,growx");

		panelPrincipal.add(this.lbSigla, "cell 0 2,alignx trailing");

		panelPrincipal.add(panel, "cell 1 2,grow");
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(fieldSigla, BorderLayout.CENTER);
		panel.add(chboxStatus, BorderLayout.EAST);

	}

	@Override
	public JComponent getFirstFocusable() {
		return fieldDescricao;
	}

	public static void main(String[] args) {

		try {
			new DialogUnidadeMedida(null).setVisible(true);

		} finally {
			Persistence.closeAll();
		}
	}

	@Override
	public AbstractDAO<UnidadeMedida> getDAO() {
		return new UnidadeMedidaDAO();
	}

	@Override
	public String getTitulo() {
		return "Unidade de Medida";
	}

    @Override
    public void preecherCampos(UnidadeMedida entidade) {
        fieldIdentificador.setText(entidade != null && entidade.getIdUnidadeMedida() != null ? entidade.getIdUnidadeMedida().toString() : null);
        fieldDescricao.setText(entidade != null ? entidade.getDescricao() : null);
        fieldSigla.setText(entidade != null ? entidade.getSigla().toString() : null);

    }

	@Override
	public void preecherEntidade(UnidadeMedida entidade) throws EJDLogicException {
		entidade.setDescricao(fieldDescricao.getText());

		entidade.setSigla(fieldSigla.getText());
		validarCamposObrigatorios(entidade);
	}

	@Override
	public void habilitarCampos(EntityState state) {
		boolean habilita = false;
		switch (state) {
			case DELETED:
			case UNMODIFIED:
				habilita = false;
				break;

			case NEW:
			case MODIFIED:
				habilita = true;
				break;
		}

		fieldIdentificador.setEnabled(false);
		fieldDescricao.setEnabled(habilita);
		fieldSigla.setEnabled(habilita);

	}

	@Override
	public ITableModelEntidade<UnidadeMedida> getTableModelEntidade() {

		return new ITableModelEntidade<UnidadeMedida>() {
			@Override
			public Object getCampoEntidade(UnidadeMedida entidade, int coluna) {
				switch (coluna) {
					case 0:
						return entidade.getIdUnidadeMedida();
					case 1:
						return entidade.getDescricao();
					case 2:
						return entidade.getSigla();
				}
				return null;
			}

			@Override
			public List<UnidadeMedida> getEntidades() {
				return dao.localizarTodos();
			}

			@Override
			public String[] getNomesColunas() {
				return new String[] { "Identificador", "Descrição", "Sigla"};
			}

		};
	}

	@Override
	public void validarCamposObrigatorios(UnidadeMedida entidade) throws EJDLogicException {
		ValidateEntity validateEntity = new ValidateEntity();

		validateEntity.validateNotNull(entidade.getDescricao(), "Descrição");
		validateEntity.validateNotNull(entidade.getSigla(), "Sigla");

		validateEntity.doValidate();

	}

}

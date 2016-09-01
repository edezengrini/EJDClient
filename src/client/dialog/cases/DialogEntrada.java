package client.dialog.cases;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import server.dao.AbstractDAO;
import server.dao.EntradaDAO;
import server.entities.Entrada;
import server.entities.Produto;
import server.entities.abstracts.EntityState;
import server.persistence.Persistence;
import arquitetura.client.components.DecimalFormattedField;
import arquitetura.client.components.textfield.EJDDateTimeField;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.util.DateUtil;
import arquitetura.common.util.ValidateEntity;
import client.dialog.DialogCrud;
import client.dialog.consultas.ConsultaProduto;
import client.dialog.util.UtilClient;
import client.enums.EnumImage;
import client.table.TableModelEntidade.ITableModelEntidade;

import common.util.UtilCommon;

public class DialogEntrada extends DialogCrud<Entrada> {
	private static final long serialVersionUID = 1L;

	private Produto produto = new Produto();

	private final JLabel labelIdentificador = new JLabel("Identificador:");
	private final JLabel labelQuantidade = new JLabel("Quantidade:");
	private final JLabel labelData = new JLabel("Data da Entrada:");
	private final JTextField textFieldIdentificador = new JTextField();
	private final JTextField textFieldQuantidade = new DecimalFormattedField(DecimalFormattedField.NUMERO4);
	private final JTextField textFieldData = new EJDDateTimeField();
	private final JLabel labelProduto = new JLabel("Produto:");
	private final JTextField textFieldProduto = new JTextField();
	private final JPanel panel = new JPanel();
	private final JButton btnConsultaProduto = new JButton();


	public DialogEntrada(Window window) {
		super(window);

		initComponents();
	}

	private void initComponents() {
		panelPrincipal.setLayout(new MigLayout("", "[82px][484px]", "[20px][20px][20px][20px]"));

		panelPrincipal.add(this.labelIdentificador, "cell 0 0,alignx right,aligny center");

		panelPrincipal.add(this.textFieldIdentificador, "cell 1 0,growx,aligny top");

		panelPrincipal.add(this.labelProduto, "cell 0 1,alignx right,aligny center");

		panelPrincipal.add(panel, "cell 1 1,grow");

		panelPrincipal.add(this.labelQuantidade, "cell 0 2,alignx right,aligny center");

		panelPrincipal.add(this.textFieldQuantidade, "cell 1 2,growx,aligny top");

		panelPrincipal.add(this.labelData, "cell 0 3,alignx left,aligny center");

		panelPrincipal.add(this.textFieldData, "cell 1 3,growx,aligny top");

		panel.setLayout(new BorderLayout(0, 0));

		panel.add(textFieldProduto, BorderLayout.CENTER);
		textFieldProduto.setColumns(10);

		btnConsultaProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				consultarProduto();
			}
		});
		btnConsultaProduto.setIcon(new ImageIcon(DialogEntrada.class.getResource(EnumImage.LOCALIZAR.getCaminho())));

		panel.add(btnConsultaProduto, BorderLayout.EAST);

		this.setModal(true);
		this.setFocusableWindowState(true);

	}

	@Override
	public JComponent getFirstFocusable() {
		return btnConsultaProduto;
	}

	public static void main(String[] args) {

		try {
			new DialogEntrada(null).setVisible(true);
		} finally {
			Persistence.closeAll();
			System.exit(0);
		}
	}

	@Override
	public AbstractDAO<Entrada> getDAO() {
		return new EntradaDAO();
	}

	@Override
	public String getTitulo() {
		return "Entrada";
	}

	@Override
	public void preecherCampos(Entrada entidade) {
		textFieldIdentificador.setText(entidade != null && entidade.getId() != null ? entidade.getId().toString() : null);
		textFieldQuantidade.setText(entidade != null ? UtilCommon.toStringNotNull(entidade.getQuantidade()) : null);
		textFieldData.setText(entidade != null ? UtilCommon.formatDate(entidade.getDataEntrada()) : null);
		if (entidade != null && entidade.getProduto() != null){
		    textFieldProduto.setText(UtilCommon.toStringNotNull(entidade.getProduto().getDescricao()));
		    produto = entidade.getProduto();

		}
	}

    @Override
	public void preecherEntidade(Entrada entidade) throws EJDLogicException {

		entidade.setProduto(produto);

		entidade.setQuantidade(new BigDecimal(UtilCommon.convertDecimal(textFieldQuantidade.getText())));

		DateUtil dataUtil = new DateUtil();

		entidade.setDataEntrada(textFieldData.getText().isEmpty() || textFieldData.getText().equals("  /  /    ") ? new Date() : dataUtil.parseDateDDMMYYYY(textFieldData.getText()));

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

		textFieldIdentificador.setEnabled(false);
		textFieldQuantidade.setEnabled(habilita);
		textFieldData.setEnabled(habilita);
		textFieldProduto.setEnabled(false);
		btnConsultaProduto.setEnabled(habilita);

	}

	@Override
	public ITableModelEntidade<Entrada> getTableModelEntidade() {

		return new ITableModelEntidade<Entrada>() {
			@Override
			public Object getCampoEntidade(Entrada entidade, int coluna) {
				switch (coluna) {
					case 0:
						return entidade.getId();
					case 1:
						return entidade.getProduto();
					case 2:
						return entidade.getQuantidade();
				}
				return null;
			}

			@Override
			public List<Entrada> getEntidades() {
				return dao.localizarTodos();
			}

			@Override
			public String[] getNomesColunas() {
				return new String[] { "Identificador", "Produto", "Quantidade" };
			}

		};
	}

	protected void consultarProduto()  {

		try {
			setCursor(UtilClient.CURSOR_WAIT);

			DialogCrud<Produto> dialogProduto = new DialogProduto(getOwner());

			ConsultaProduto consultaProduto = new ConsultaProduto();

			produto = consultaProduto.consultarProduto(dialogProduto);

			if (produto != null) {
			    textFieldProduto.setText(produto.getDescricao());
			}

		} catch (Exception e) {
			reportException(e);
		} finally {
			setCursor(UtilClient.CURSOR_DEFAULT);
		}
	}

	@Override
	public void validarCamposObrigatorios(Entrada entidade) throws EJDLogicException {
		ValidateEntity validateEntity = new ValidateEntity();

		validateEntity.validateNotNull(entidade.getProduto(), "Produto");
		validateEntity.validateNotNull(entidade.getQuantidade(), "Quantidade");

		validateEntity.doValidate();
	}



}

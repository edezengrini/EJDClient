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
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import server.dao.AbstractDAO;
import server.dao.SaidaDAO;
import server.entities.Pessoa;
import server.entities.PessoaFisica;
import server.entities.PessoaJuridica;
import server.entities.Produto;
import server.entities.Saida;
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

public class DialogSaida extends DialogCrud<Saida> {
	private static final long serialVersionUID = 1L;

	private Produto produto = new Produto();
	private Pessoa pessoa;

	private final JLabel labelIdentificador = new JLabel("Identificador:");
	private final JLabel labelQuantidade = new JLabel("Quantidade:");
	private final JLabel labelData = new JLabel("Data da Saida:");
	private final JTextField fieldIdentificador = new JTextField();
	private final JTextField fieldQuantidade = new DecimalFormattedField(DecimalFormattedField.NUMERO4);
	private final JTextField fieldData = new EJDDateTimeField();
	private final JLabel labelProduto = new JLabel("Produto:");
	private final JTextField fieldProduto = new JTextField();
	private final JPanel panelProduto = new JPanel();
	private final JButton btnConsultaProduto = new JButton();
	private final JLabel labelPessoa = new JLabel("Pessoa:");
	private final JTextField fieldPessoa = new JTextField();
	private final JPanel panelPessoa = new JPanel();
	private final JButton btnConsultaPessoa = new JButton("");


	public DialogSaida(Window window) {
		super(window);

		initComponents();
	}

	private void initComponents() {
		panelPrincipal.setLayout(new MigLayout("", "[82px][484px,grow]", "[20px][][20px][20px][20px]"));

		panelPrincipal.add(this.labelIdentificador, "cell 0 0,alignx right,aligny center");

		panelPrincipal.add(this.fieldIdentificador, "cell 1 0,growx,aligny top");
		labelPessoa.setHorizontalAlignment(SwingConstants.RIGHT);

		panelPrincipal.add(labelPessoa, "cell 0 1,alignx trailing");

		panelPrincipal.add(panelPessoa, "cell 1 1,grow");

		panelPrincipal.add(this.labelProduto, "cell 0 2,alignx right,aligny center");

		panelPrincipal.add(panelProduto, "cell 1 2,grow");

		panelPrincipal.add(this.labelQuantidade, "cell 0 3,alignx right,aligny center");

		panelPrincipal.add(this.fieldQuantidade, "cell 1 3,growx,aligny top");

		panelPrincipal.add(this.labelData, "cell 0 4,alignx left,aligny center");

		panelPrincipal.add(this.fieldData, "cell 1 4,growx,aligny top");

		panelProduto.setLayout(new BorderLayout(0, 0));

		panelProduto.add(fieldProduto, BorderLayout.CENTER);
		fieldProduto.setColumns(10);

		btnConsultaProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				consultarProduto();
			}
		});
		btnConsultaProduto.setIcon(new ImageIcon(DialogSaida.class.getResource(EnumImage.LOCALIZAR.getCaminho())));

		panelProduto.add(btnConsultaProduto, BorderLayout.EAST);

        panelPessoa.setLayout(new BorderLayout(0, 0));
        panelPessoa.add(fieldPessoa);
        fieldPessoa.setColumns(10);
        btnConsultaPessoa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultarPessoa();
            }
        });
        btnConsultaPessoa.setIcon(new ImageIcon(DialogSaida.class.getResource(EnumImage.LOCALIZAR.getCaminho())));

        panelPessoa.add(btnConsultaPessoa, BorderLayout.EAST);

		this.setModal(true);
		this.setFocusableWindowState(true);

	}

	@Override
	public JComponent getFirstFocusable() {
		return btnConsultaProduto;
	}

	public static void main(String[] args) {

		try {
			new DialogSaida(null).setVisible(true);
		} finally {
			Persistence.closeAll();
			System.exit(0);
		}

	}

	@Override
	public AbstractDAO<Saida> getDAO() {
		return new SaidaDAO();
	}

	@Override
	public String getTitulo() {
		return "Saída";
	}

	@Override
	public void preecherCampos(Saida entidade) {
		fieldIdentificador.setText(entidade != null && entidade.getId() != null ? entidade.getId().toString() : null);
		fieldQuantidade.setText(entidade != null ? UtilCommon.toStringNotNull(entidade.getQuantidade()) : null);
		fieldData.setText(entidade != null ? UtilCommon.formatDate(entidade.getDataSaida()) : null);
		if (entidade != null && entidade.getProduto() != null){
		    fieldProduto.setText(UtilCommon.toStringNotNull(entidade.getProduto().getDescricao()));
		    produto = entidade.getProduto();

		}

        if (entidade != null && entidade.getPessoa() != null && entidade.getPessoa().isFisica()) {
            fieldPessoa.setText(UtilCommon.toStringNotNull(entidade.getPessoa().getPessoaFisica().getNome()));
            pessoa = new PessoaFisica();
            pessoa = entidade.getPessoa().getPessoaFisica();
        }

        if (entidade != null && entidade.getPessoa() != null && entidade.getPessoa().isJuridica()) {
            fieldPessoa.setText(UtilCommon.toStringNotNull(entidade.getPessoa().getPessoaJuridica().getRazaoSocial()));
            pessoa = new PessoaJuridica();
            pessoa = entidade.getPessoa().getPessoaJuridica();
        }

	}

    @Override
	public void preecherEntidade(Saida entidade) throws EJDLogicException {

		entidade.setProduto(produto);

		entidade.setQuantidade(new BigDecimal(UtilCommon.convertDecimal(fieldQuantidade.getText())));

		DateUtil dataUtil = new DateUtil();

		entidade.setDataSaida(fieldData.getText().isEmpty() || fieldData.getText().equals("  /  /    ") ? new Date() : dataUtil.parseDateDDMMYYYY(fieldData.getText()));

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
		fieldQuantidade.setEnabled(habilita);
		fieldData.setEnabled(habilita);
		fieldProduto.setEnabled(false);
		btnConsultaProduto.setEnabled(habilita);

	}

	@Override
	public ITableModelEntidade<Saida> getTableModelEntidade() {

		return new ITableModelEntidade<Saida>() {
			@Override
			public Object getCampoEntidade(Saida entidade, int coluna) {
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
			public List<Saida> getEntidades() {
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
			    fieldProduto.setText(produto.getDescricao());
			}

		} catch (Exception e) {
			reportException(e);
		} finally {
			setCursor(UtilClient.CURSOR_DEFAULT);
		}
	}

    protected void consultarPessoa()  {
//
//        try {
//            setCursor(UtilClient.CURSOR_WAIT);
//
//            DialogCrud<Produto> dialogProduto = new DialogProduto(getOwner());
//
//            ConsultaProduto consultaProduto = new ConsultaProduto();
//
//            produto = consultaProduto.consultarProduto(dialogProduto);
//
//            if (produto != null) {
//                fieldProduto.setText(produto.getDescricao());
//            }
//
//        } catch (Exception e) {
//            reportException(e);
//        } finally {
//            setCursor(UtilClient.CURSOR_DEFAULT);
//        }
    }

	@Override
	public void validarCamposObrigatorios(Saida entidade) throws EJDLogicException {
		ValidateEntity validateEntity = new ValidateEntity();

		validateEntity.validateNotNull(entidade.getProduto(), "Produto");
		validateEntity.validateNotNull(entidade.getQuantidade(), "Quantidade");

		validateEntity.doValidate();
	}



}

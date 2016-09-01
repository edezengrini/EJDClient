package client.dialog.cases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import server.dao.AbstractDAO;
import server.dao.ProdutoDAO;
import server.ejb.ProdutoEJB;
import server.entities.Flag;
import server.entities.Grupo;
import server.entities.Produto;
import server.entities.UnidadeMedida;
import server.entities.abstracts.EntityState;
import arquitetura.client.binding.annotation.TextBindingAnnot;
import arquitetura.client.components.panel.EJDCheckBox;
import arquitetura.client.components.panel.EJDPanelCodigoDescricao;
import arquitetura.client.components.textfield.EJDDateTimeField;
import arquitetura.client.components.textfield.EJDDecimalField;
import arquitetura.client.components.textfield.EJDTextField;
import arquitetura.client.components.textfield.EnumFiltroTextField;
import arquitetura.client.components.textfield.EnumMascarasDatas;
import arquitetura.common.beans.attribute.Attribute.UPDATABLE_TYPE;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.util.DateUtil;
import arquitetura.common.util.ValidateEntity;
import client.dialog.DialogCrud;
import client.dialog.consultas.ConsultaGrupo;
import client.dialog.util.UtilClient;
import client.enums.EnumImage;
import client.layout.VerticalFlowLayout;
import client.table.TableModelEntidade.ITableModelEntidade;

import common.base.FlagBase;
import common.util.UtilCommon;

public class DialogPessoaFisica extends DialogCrud<Produto>{

    private static final long serialVersionUID = 1L;
    private EJDPanelCodigoDescricao panelCodigoNome;
    private JPanel panelEstoque;
    private JPanel panelEndereco;
    private JPanel panelGenero;
    private JPanel panelCpf;
    private JPanel panelRg;
    private JPanel panelDadosRG;
    private JPanel panelRgOrgaoEmissor;
    private JPanel panelRgEmissao;
    private JPanel panelNascimento;
    private JPanel panelStatus;

    private EJDTextField fieldCodigo;
    private EJDTextField fieldNome;
    private EJDTextField fieldCodigoEndereco;
    private EJDTextField fieldDescricaoEndereco;
    private EJDDateTimeField fieldNascimento;
    private EJDDateTimeField fieldRgEmissao;
    private EJDTextField fieldGenero;
    @TextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.WHILE_NEW,
            tamanho = 14)
    private EJDTextField fieldCpf;
    @TextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.WHILE_NEW,
            tamanho = 14)
    private EJDTextField fieldRg;
    @TextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.WHILE_NEW,
            tamanho = 5)
    private EJDTextField fieldRgOrgaoEmissor;
    private JButton btnEndereco;
    private EJDCheckBox chkStatus;

    private UnidadeMedida unidadeMedida = new UnidadeMedida();

    private Grupo grupo = new Grupo();

    private ConsultaGrupo consultaGrupo = new ConsultaGrupo();

    private ProdutoEJB produtoEJB = new ProdutoEJB();

    public DialogPessoaFisica(Window window) {
        super(window);
        initComponents();
    }

    private void initComponents() {
        panelPrincipal.setLayout(new VerticalFlowLayout());

        panelCodigoNome = new EJDPanelCodigoDescricao("Código", "Nome");
        panelEstoque = new JPanel();
        panelEndereco = new JPanel();
        panelEndereco.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Endere\u00E7o", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

        panelPrincipal.add(panelCodigoNome);

        fieldCodigo = new EJDTextField(EnumFiltroTextField.NUMEROS);
        panelCodigoNome.getPanelCodigo().add(fieldCodigo);
        fieldCodigo.setEnabled(false);
        fieldNome = new EJDTextField(EnumFiltroTextField.LETRAS_NUMEROS);
        panelCodigoNome.getPanelDescricao().add(fieldNome);

        panelPrincipal.add(panelEstoque);
        panelEstoque.setLayout(new GridLayout(1, 0, 0, 0));

        panelGenero = new JPanel();
        panelGenero.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "G\u00EAnero", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelEstoque.add(panelGenero);
        panelGenero.setLayout(new BorderLayout(0, 0));

        fieldGenero = new EJDTextField(EnumFiltroTextField.MAIUSCULAS);
        panelGenero.add(fieldGenero);

        panelCpf = new JPanel();
        panelCpf.setBorder(new TitledBorder(null, "CPF", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelEstoque.add(panelCpf);
        panelCpf.setLayout(new BorderLayout(0, 0));

        fieldCpf = new EJDTextField(EnumFiltroTextField.NENHUM);
        panelCpf.add(fieldCpf);

        panelStatus = new JPanel();
        panelStatus.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelEstoque.add(panelStatus);
        panelStatus.setLayout(new BorderLayout(0, 0));

        chkStatus = new EJDCheckBox("Ativo");
        chkStatus.setHorizontalAlignment(SwingConstants.CENTER);
        panelStatus.add(chkStatus);

        panelDadosRG = new JPanel();
        panelDadosRG.setBorder(null);
        panelPrincipal.add(panelDadosRG);
        panelDadosRG.setLayout(new GridLayout(1, 0, 0, 0));

        panelRg = new JPanel();
        panelDadosRG.add(panelRg);
        panelRg.setBorder(new TitledBorder(null, "RG", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelRg.setLayout(new BorderLayout(0, 0));

        fieldRg = new EJDDecimalField(10, 4);
        panelRg.add(fieldRg);

        panelRgOrgaoEmissor = new JPanel();
        panelRgOrgaoEmissor.setBorder(new TitledBorder(null, "\u00d3rg\u00e3o Emissor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelDadosRG.add(panelRgOrgaoEmissor);
        panelRgOrgaoEmissor.setLayout(new BorderLayout(0, 0));

        fieldRgOrgaoEmissor = new EJDDecimalField(10, 4);
        panelRgOrgaoEmissor.add(fieldRgOrgaoEmissor);

        panelRgEmissao = new JPanel();
        panelRgEmissao.setBorder(new TitledBorder(null, "Data Emiss\u00e3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelDadosRG.add(panelRgEmissao);
        panelRgEmissao.setLayout(new BorderLayout(0, 0));

        fieldRgEmissao = new EJDDateTimeField(EnumMascarasDatas.DDMMYYYY);
        panelRgEmissao.add(fieldRgEmissao);

        panelNascimento = new JPanel();
        panelNascimento.setBorder(new TitledBorder(null, "Nascimento", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelDadosRG.add(panelNascimento);
        panelNascimento.setLayout(new BorderLayout(0, 0));

        fieldNascimento = new EJDDateTimeField(EnumMascarasDatas.DDMMYYYY);
        panelNascimento.add(fieldNascimento);

        panelPrincipal.add(panelEndereco);
        panelEndereco.setLayout(new BorderLayout(0, 0));

        fieldCodigoEndereco = new EJDTextField(EnumFiltroTextField.NUMEROS);
        panelEndereco.add(fieldCodigoEndereco, BorderLayout.WEST);

        fieldDescricaoEndereco = new EJDTextField(EnumFiltroTextField.LETRAS_NUMEROS);
        panelEndereco.add(fieldDescricaoEndereco, BorderLayout.CENTER);

        btnEndereco = new JButton("");
        btnEndereco.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                consultarGrupo();
            }
        });
        btnEndereco.setIcon(new ImageIcon(DialogPessoaFisica.class.getResource(EnumImage.LOCALIZAR.getCaminho())));
        panelEndereco.add(btnEndereco, BorderLayout.EAST);

    }

    protected void consultarGrupo() {
        try {
            setCursor(UtilClient.CURSOR_WAIT);

            DialogCrud<Grupo> dialogGrupo = new DialogGrupo(getOwner());

            grupo = consultaGrupo.consultarGrupos(dialogGrupo);

            if (grupo != null) {
                fieldCodigoEndereco.setText(UtilCommon.toStringNotNull(grupo.getCodigo()));
                fieldDescricaoEndereco.setText(grupo.getDescricao());
            }

        } catch (Exception e) {
            reportException(e);
        } finally {
            setCursor(UtilClient.CURSOR_DEFAULT);
        }
    }

    @Override
    public AbstractDAO<Produto> getDAO() {
        return new ProdutoDAO();
    }

    @Override
    public void preecherEntidade(Produto entidade) throws EJDLogicException {
        entidade.setCodigo(produtoEJB.findMaiorIdentificador() != null ? produtoEJB.findMaiorIdentificador().getIdProduto() + 1 : 1);
        entidade.setDescricao(fieldNome.getText());
        entidade.setFlagStatus(chkStatus.isSelected() ? new Flag(FlagBase.PRODUTO_IDFLAGSTATUS_ATIVO) : new Flag(FlagBase.PRODUTO_IDFLAGSTATUS_BAIXADO));
        entidade.setEstoque(new BigDecimal(0));
        entidade.setEstoqueMinimo(UtilCommon.convertBigDecimalSomentePonto(fieldCpf.getText()));
        entidade.setEstoqueMaximo(UtilCommon.convertBigDecimalSomentePonto(fieldRg.getText()));
        entidade.setValorUnitarioEntrada(UtilCommon.convertBigDecimalSomentePonto(fieldRgOrgaoEmissor.getText()));
        entidade.setValorUnitarioSaida(UtilCommon.convertBigDecimalSomentePonto(fieldRgEmissao.getText()));

        DateUtil dataUtil = new DateUtil();
        entidade.setDataUltimaCompra(fieldNascimento.getText().isEmpty() || fieldNascimento.getText().equals("  /  /    ") ? new Date() : dataUtil.parseDateDDMMYYYY(fieldNascimento.getText()));

        entidade.setGrupo(grupo);
        entidade.setUnidadeMedida(unidadeMedida);
        validarCamposObrigatorios(entidade);
        produtoEJB.validarEstoque(entidade);
        produtoEJB.validarValor(entidade);
    }

    @Override
    public void preecherCampos(Produto entidade) {
        if (entidade != null){
            fieldCodigo.setText(entidade.getCodigo() != null ? entidade.getCodigo().toString() : null);
            fieldNome.setText(entidade.getDescricao());
            fieldRgOrgaoEmissor.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getValorUnitarioEntrada()));
            fieldRgEmissao.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getValorUnitarioSaida()));
            fieldGenero.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getEstoque()));
            fieldCpf.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getEstoqueMinimo()));
            fieldRg.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getEstoqueMaximo()));
            if (entidade.getFlagStatus() != null) {
                chkStatus.setSelected(entidade != null ? (entidade.getFlagStatus().getIdFlag().compareTo(FlagBase.PRODUTO_IDFLAGSTATUS_ATIVO) == 0 ? true : false) : false);
                unidadeMedida = entidade.getUnidadeMedida();
                grupo = entidade.getGrupo();
                fieldCodigoEndereco.setText(grupo != null ? grupo.getCodigo().toString() : null);
                fieldDescricaoEndereco.setText(grupo != null ? grupo.getDescricao() : null);
            } else {
                fieldCodigoEndereco.setText("");
                fieldDescricaoEndereco.setText("");
            }
        }
    }

    @Override
    public void habilitarCampos(EntityState state) {
        boolean habilita = false;
        boolean ativo = chkStatus.isSelected();
        switch (state) {
        case DELETED:
        case UNMODIFIED:
            habilita = false;
            break;
        case NEW:
            ativo = true;
            habilita = true;
            break;
        case MODIFIED:
            ativo = chkStatus.isSelected();
            habilita = true;
            break;
        }

        fieldCodigo.setEnabled(false);
        fieldNome.setEnabled(habilita && ativo);
        fieldRgOrgaoEmissor.setEnabled(habilita && ativo);
        fieldRgEmissao.setEnabled(habilita && ativo);
        chkStatus.setEnabled(habilita);
        fieldCodigoEndereco.setEnabled(false);
        fieldDescricaoEndereco.setEnabled(false);
        btnEndereco.setEnabled(habilita && ativo);
        fieldGenero.setEnabled(false);
        fieldCpf.setEnabled(habilita && ativo);
        fieldRg.setEnabled(habilita && ativo);
        fieldNascimento.setEnabled(habilita && ativo);

    }

    @Override
    public ITableModelEntidade<Produto> getTableModelEntidade() {
        return new ITableModelEntidade<Produto>() {
            @Override
            public Object getCampoEntidade(Produto entidade, int coluna) {
                switch (coluna) {
                case 0:
                    return entidade.getCodigo();
                case 1:
                    return entidade.getDescricao();
                case 2:
                    return UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getValorUnitarioEntrada());
                case 3:
                    dao.sincronizar(entidade);
                    return entidade.getFlagStatus().getDescricao();
                }
                return null;
            }

            @Override
            public List<Produto> getEntidades() {
                return dao.localizarTodosOrderBy();
            }

            @Override
            public String[] getNomesColunas() {
                return new String[] { "Código", "Descrição", "Valor Unitário de Entrada", "Status" };
            }

        };
    }

    @Override
    public void validarCamposObrigatorios(Produto entidade) throws EJDLogicException {
        ValidateEntity validateEntity = new ValidateEntity();

        validateEntity.validateNotNullOrEmpty(entidade.getDescricao(), "Descrição");
        validateEntity.validateNotNullOrEmpty(entidade.getEstoqueMinimo(), "Estoque Mínimo");
        validateEntity.validateNotNullOrEmpty(entidade.getEstoqueMaximo(), "Estoque Máximo");
        validateEntity.validateNotNullOrEmpty(entidade.getUnidadeMedida().getIdUnidadeMedida(), "Unidade de Medida");
        validateEntity.validateNotNullOrEmpty(entidade.getGrupo().getIdGrupo(), "Grupo");
        validateEntity.validateNotNullOrEmpty(entidade.getValorUnitarioEntrada(), "Valor Unitário de Entrada");
        validateEntity.validateNotNullOrEmpty(entidade.getValorUnitarioSaida(), "Valor Unitário de Saída");

        validateEntity.doValidate();

    }

    @Override
    public JComponent getFirstFocusable() {
        return fieldNome;
    }

    @Override
    public String getTitulo() {
        return "Pessoa Física";
    }

}

package client.dialog.cases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ejb.AfterCompletion;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import arquitetura.client.binding.annotation.TextBindingAnnot;
import arquitetura.client.components.panel.EJDCheckBox;
import arquitetura.client.components.panel.EJDPanelCodigoDescricao;
import arquitetura.client.components.textfield.EJDDateTimeField;
import arquitetura.client.components.textfield.EJDDecimalField;
import arquitetura.client.components.textfield.EJDTextField;
import arquitetura.client.components.textfield.EnumFiltroTextField;
import arquitetura.client.components.textfield.EnumMascarasDatas;
import arquitetura.common.beans.attribute.Attribute.UPDATABLE_TYPE;
import arquitetura.common.exception.EJDException;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.util.DateUtil;
import arquitetura.common.util.ValidateEntity;
import client.dialog.DialogCrud;
import client.enums.EnumImage;
import client.layout.VerticalFlowLayout;
import client.table.TableModelEntidade.ITableModelEntidade;
import common.base.FlagBase;
import server.dao.AbstractDAO;
import server.dao.PessoaDAO;
import server.dao.PessoaFisicaDAO;
import server.entities.Flag;
import server.entities.Pessoa;
import server.entities.PessoaFisica;
import server.entities.abstracts.EntityState;

public class DialogPessoaFisica extends DialogCrud<PessoaFisica>{

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
    @TextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.ALWAYS,
            tamanho = 14)
    private EJDTextField fieldGenero;
    @TextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.ALWAYS,
            tamanho = 14)
    private EJDTextField fieldCpf;
    @TextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.ALWAYS,
            tamanho = 14)
    private EJDTextField fieldRg;
    @TextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.ALWAYS,
            tamanho = 5)
    private EJDTextField fieldRgOrgaoEmissor;
    @TextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.ALWAYS,
            tamanho = 15)
    private EJDTextField fieldFone;
    private JButton btnEndereco;
    private EJDCheckBox chkStatus;
    private JPanel panelFone;
    private PessoaDAO pessoaDAO = new PessoaDAO();
    private PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO();

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
        
        panelFone = new JPanel();
        panelFone.setBorder(new TitledBorder(null, "Telefone", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelEstoque.add(panelFone);
        panelFone.setLayout(new BorderLayout(0, 0));
        
        fieldFone = new EJDTextField();
        
        fieldFone.setMascara(inserirMascara());
        panelFone.add(fieldFone);
        fieldFone.setColumns(10);

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

        fieldRgOrgaoEmissor = new EJDTextField(EnumFiltroTextField.MAIUSCULAS);
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
                consultarEndereco();
            }
        });
        btnEndereco.setIcon(new ImageIcon(DialogPessoaFisica.class.getResource(EnumImage.LOCALIZAR.getCaminho())));
        panelEndereco.add(btnEndereco, BorderLayout.EAST);

    }
    
    private String inserirMascara() {
        String retorno = "";
        MaskFormatter mascara;
        try {
            
            mascara = new MaskFormatter("(##)#####-####");
            mascara.setPlaceholderCharacter('_');
            retorno = mascara.getMask();
        } catch (ParseException e) {
            e.printStackTrace();
        } 
        return retorno;
    }
            
    protected void consultarEndereco() {
//        try {
//            setCursor(UtilClient.CURSOR_WAIT);
//
//            DialogCrud<Grupo> dialogGrupo = new DialogGrupo(getOwner());
//
//            grupo = consultaGrupo.consultarGrupos(dialogGrupo);
//
//            if (grupo != null) {
//                fieldCodigoEndereco.setText(UtilCommon.toStringNotNull(grupo.getCodigo()));
//                fieldDescricaoEndereco.setText(grupo.getDescricao());
//            }
//
//        } catch (Exception e) {
//            reportException(e);
//        } finally {
//            setCursor(UtilClient.CURSOR_DEFAULT);
//        }
    }

    @Override
    public AbstractDAO<PessoaFisica> getDAO() {
        return new PessoaFisicaDAO();
    }
    
    @Override
    public void preecherEntidade(PessoaFisica entidade) throws EJDLogicException {
        entidade.setNome(fieldNome.getText());
        entidade.setCpf(fieldCpf.getText());
        entidade.setRg(fieldRg.getText());
        entidade.setRgOrgaoEmissor(fieldRgOrgaoEmissor.getText());
        entidade.setGenero(fieldGenero.getText());

        DateUtil dataUtil = new DateUtil();
        entidade.setRgEmissao(fieldRgEmissao.getText().isEmpty() || fieldRgEmissao.getText().equals("  /  /    ") ? new Date() : dataUtil.parseDateDDMMYYYY(fieldRgEmissao.getText()) );
        entidade.setNascimento(fieldNascimento.getText().isEmpty() || fieldNascimento.getText().equals("  /  /    ") ? new Date() : dataUtil.parseDateDDMMYYYY(fieldNascimento.getText()));
        validarCamposObrigatorios(entidade);
        
//        entidade = gerarPessoa(entidade);
    }

    private PessoaFisica gerarPessoa(PessoaFisica entidade) {
        
        Pessoa pessoaFisica = pessoaDAO.findByPessoaFisica(entidade.getIdPessoaFisica());
        
        try {
            if (pessoaFisica != null) {
                alterarPessoa(entidade, pessoaFisica);
            } else {
//                pessoaFisicaDAO.flush();
                entidade = pessoaFisicaDAO.editar(entidade);
                inserirPessoa(entidade, pessoaFisica);
                entidade = null;
            }
        } catch (EJDException e) {
            e.printStackTrace();
        }
        
        return entidade;
    }

    private void inserirPessoa(PessoaFisica entidade, Pessoa pessoa) throws EJDException {
        
        pessoa = new Pessoa();
        pessoa.setPessoaFisica(entidade);
        pessoa.setFone(fieldFone.getText());
        pessoa.setFlagStatus(chkStatus.isSelected() ? new Flag(FlagBase.PESSOA_IDFLAGSTATUS_ATIVO) : new Flag(FlagBase.PESSOA_IDFLAGSTATUS_INATIVO));

        
        try {
            pessoaDAO.inserir(pessoa);
        } catch (EJDException e) {
            e.printStackTrace();
        }
    }
    
    private void alterarPessoa(PessoaFisica entidade, Pessoa pessoa) throws EJDException {
        pessoa.setPessoaFisica(entidade);
        pessoa.setFone(fieldFone.getText());
        pessoa.setFlagStatus(chkStatus.isSelected() ? new Flag(FlagBase.PESSOA_IDFLAGSTATUS_ATIVO) : new Flag(FlagBase.PESSOA_IDFLAGSTATUS_INATIVO));
        
        try {
            pessoaDAO.editar(pessoa);
        } catch (EJDException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void preecherCampos(PessoaFisica entidade) {
        if (entidade != null){
            gerarPessoa(entidade);
            fieldNome.setText(entidade.getNome());
            fieldRgOrgaoEmissor.setText(entidade.getRgOrgaoEmissor());
//            fieldRgEmissao.setText(entidade.getRgEmissao());
            fieldGenero.setText(entidade.getGenero());
            fieldCpf.setText(entidade.getCpf());
            fieldRg.setText(entidade.getRg());
//            if (entidade.getFlagStatus() != null) {
//                chkStatus.setSelected(entidade != null ? (entidade.getFlagStatus().getIdFlag().compareTo(FlagBase.PRODUTO_IDFLAGSTATUS_ATIVO) == 0 ? true : false) : false);
//                fieldCodigoEndereco.setText(grupo != null ? grupo.getCodigo().toString() : null);
//                fieldDescricaoEndereco.setText(grupo != null ? grupo.getDescricao() : null);
//            } else {
//                fieldCodigoEndereco.setText("");
//                fieldDescricaoEndereco.setText("");
//            }
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
        fieldNome.setEnabled(habilita);
        fieldRgOrgaoEmissor.setEnabled(habilita);
        fieldRgEmissao.setEnabled(habilita);
        chkStatus.setEnabled(habilita);
        fieldCodigoEndereco.setEnabled(false);
        fieldDescricaoEndereco.setEnabled(false);
        btnEndereco.setEnabled(habilita && ativo);
        fieldGenero.setEnabled(habilita);
        fieldCpf.setEnabled(habilita);
        fieldRg.setEnabled(habilita);
        fieldNascimento.setEnabled(habilita);
        fieldFone.setEnabled(habilita);

    }

    @Override
    public ITableModelEntidade<PessoaFisica> getTableModelEntidade() {
        return new ITableModelEntidade<PessoaFisica>() {
            @Override
            public Object getCampoEntidade(PessoaFisica entidade, int coluna) {
                switch (coluna) {
                case 0:
                    return entidade.getIdPessoaFisica();
                case 1:
                    return entidade.getNome();
//                case 2:
//                    return UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getValorUnitarioEntrada());
//                case 3:
//                    dao.sincronizar(entidade);
//                    return entidade.getFlagStatus().getDescricao();
                }
                return null;
            }

            @Override
            public List<PessoaFisica> getEntidades() {
                return dao.localizarTodosOrderBy();
            }

            @Override
            public String[] getNomesColunas() {
                return new String[] { "Código", "Nome" };
            }

        };
    }

    @Override
    public void validarCamposObrigatorios(PessoaFisica entidade) throws EJDLogicException {
        ValidateEntity validateEntity = new ValidateEntity();

        validateEntity.validateNotNullOrEmpty(entidade.getNome(), "Nome");
        validateEntity.validateNotNullOrEmpty(entidade.getCpf(), "CPF");
        validateEntity.validateNotNullOrEmpty(entidade.getNascimento(), "Nascimento");

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

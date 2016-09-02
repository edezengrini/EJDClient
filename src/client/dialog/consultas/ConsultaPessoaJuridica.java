package client.dialog.consultas;

import javax.inject.Inject;

import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.exception.EnumEJDException;
import client.dialog.DialogCrud;
import client.dialog.DialogLocalizar;
import server.dao.PessoaJuridicaDAO;
import server.ejb.PessoaJuridicaEJB;
import server.entities.PessoaJuridica;

public class ConsultaPessoaJuridica {

    PessoaJuridica pessoaJuridica = new PessoaJuridica();
    PessoaJuridicaDAO pessoaJuridicaDao = new PessoaJuridicaDAO();

    @Inject
    private PessoaJuridicaEJB pessoaJuridicaEJB = new PessoaJuridicaEJB();

    public PessoaJuridica consultarPessoaJuridica(DialogCrud<PessoaJuridica> dialogPessoaJuridica) throws EJDLogicException {

        DialogLocalizar<PessoaJuridica> formLocalizar = new DialogLocalizar<PessoaJuridica>(dialogPessoaJuridica, pessoaJuridicaDao.localizarTodos());
        formLocalizar.setVisible(true);

        PessoaJuridica entidadeSelecionada = formLocalizar.getEntidadeSelecionada();

        if (entidadeSelecionada != null) {
            pessoaJuridica = pessoaJuridicaEJB.find(entidadeSelecionada.getIdPessoaJuridica());
        }

        if (pessoaJuridica == null) {
            throw new EJDLogicException(EnumEJDException.ENTITY_FIND, "Pessoa Jurídica");
        }

        return pessoaJuridica;
    }

}

package com.teste.exemplo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.teste.exemplo.model.Produto;
import com.teste.exemplo.model.exception.ResourceNotFoundException;
import com.teste.exemplo.repository.ProdutoRepository;
import com.teste.exemplo.shared.ProdutoDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Metodo para retornar uma lista de produtos.
     * 
     * @return Lista de produtos.
     */
    public List<ProdutoDTO> obterTodos() {
        // Retorna uma lista de produtos
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream().map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Metodo que retorna o produto encontrado pelo seu Id.
     * @param id do produto que será localizado.
     * @return Retorna um produto caso tenha seja encontrado.
     */
    public Optional<ProdutoDTO> obterPorId(Integer id){
       //Obtendo optional de produto por id
       Optional<Produto> produto = produtoRepository.findById(id);
       //se não encontrar lança exception
       if(produto.isEmpty()){
            throw new ResourceNotFoundException("Produto com id: "+id+" não encontrado");   
       }else{
            //convertendo em um dto
            ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);
            //retornando um optional do dto
            return Optional.of(dto);
       }
    }

    /**
     * Metodo para adicionar produto na lista.
     * 
     * @param produto que será adicionado.
     * @return Retorna o produto que foi adicionado na lista.
     */
    public ProdutoDTO adicionar(ProdutoDTO produtoDTO) {
        //Removendo o id para fazer o cadastro
        produtoDTO.setId(null);
        //Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();
        //Converter o DTO em um Produto
        Produto produto = mapper.map(produtoDTO, Produto.class);
        //Salvar no banco
        produto = produtoRepository.save(produto);
        //Retornar DTO atualizado
        produtoDTO.setId(produto.getId());
        return produtoDTO;
    }

    /**
     * Metodo para deletar o produto por id.
     * 
     * @param id do produto a ser deletado.
     */
    public void deletar(Integer id) {
        //verifica se existe
        Optional<Produto> produto = produtoRepository.findById(id);
        //Se não existir lança uma exception
        if(produto.isEmpty()){
            throw new ResourceNotFoundException("Não foi possivel deletar o produto com o id: "+id+" - Produto não existe");
        }
        //deleta por id
        produtoRepository.deleteById(id);
    }

    /**
     * Metodo para atualziar o produto na lista.
     * 
     * @param produto que será aualizado.
     * @param id      do produto.
     * @return Retorna o produto após atualizar a lista.
     */
    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDTO) {
        //Passar o id para o produtoDTO
        produtoDTO.setId(id);
        //Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();
        //Converter o DTO em um Produto
        Produto produto = mapper.map(produtoDTO, Produto.class);
        //Salvar no banco
        produto = produtoRepository.save(produto);
        //Retorna o produto convertido em DTO
        return produtoDTO;
    }

}

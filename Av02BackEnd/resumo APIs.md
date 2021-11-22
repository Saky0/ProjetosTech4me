# Vendas



1. **Obter** Todas as Vendas

   * **Endpoint:** /api/vendas/

     

2. **Obter** Venda por Id

   * **Endpoint:** /api/vendas/{id}

     

3. **Inserir** Venda

   * **Endpoint:** /api/vendas/

   * **JSON**: 

     {

       "dataVenda": "yyyy-MM-dd",

       "valorTotal": 0,

       "produtosVendidos": [ ]

     } 
     
     
   
4. **Atualizar** Venda

   * **Endpoint:** /api/vendas/{id}

   * **JSON**: 

     {

       "dataVenda": "yyyy-MM-dd",

       "valorTotal": 0,

       "produtosVendidos": [ ]

     } 

     

5. **Remover** Venda

   * **Endpoint:** /api/vendas/{id}

     

6. **Obter** Vendas por período

   * **Endpoint:** /api/vendas/periodo/
   * **JSON**: 

​			{

  			"dataInicial": "yyyy-MM-dd",

 			 "dataFinal": "yyyy-MM-dd"

​			}





# Produto



1. **Obter** Todos os Produtos

   * **Endpoint:** /api/produtos/

     

2. **Obter** Produto por Id

   * **Endpoint:** /api/vendas/{id}

     

3. **Inserir** Produto

   * **Endpoint:** /api/produtos/

   * **JSON**: 

     {

        "codigo":  0,

     ​    "descricao": "",

     ​    "valor":  0.0,

     ​    "quantidadeEstoque": 0

     } 

     

4. **Atualizar** Produto

   * **Endpoint:** /api/produtos/{id}

   * **JSON**: 

     {

        "codigo":  0,

     ​    "descricao": "",

     ​    "valor":  0.0,

     ​    "quantidadeEstoque": 0

     } 

     

5. **Remover** Produto

   * **Endpoint:** /api/produtos/{id}

     

6. **Obter** por nome Em comum

   * **Endpoint:** /api/produtos/nome/{nome}
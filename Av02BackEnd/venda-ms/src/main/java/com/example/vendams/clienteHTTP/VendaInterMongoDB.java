package com.example.vendams.clienteHTTP;

import java.util.ArrayList;
import java.util.List;

import com.example.vendams.interfaces.VendaBDConnect;
import com.example.vendams.shared.VendaDto;
import com.example.vendams.utility.CompararVendaPorData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class VendaInterMongoDB implements VendaBDConnect{

    private RestTemplate resTemplate = new RestTemplate();

    @Override
    public List<VendaDto> obterTodasVendas() throws HttpServerErrorException{
        // TODO Auto-generated method stub
        List<VendaDto> vendasEncontrados = resTemplate.exchange("http://localhost:8011/venda-ms/api/vendas", 
            HttpMethod.GET, 
            null, 
            new ParameterizedTypeReference<List<VendaDto>>() {}).getBody();

        if(!vendasEncontrados.isEmpty()) {
            vendasEncontrados.sort(new CompararVendaPorData());

            return vendasEncontrados;
                
        }

        return new ArrayList<>();
    }

    @Override
    public List<VendaDto> obterVendasPorPeriodo() throws HttpServerErrorException{
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VendaDto inserirNovaVenda(VendaDto vendaNova) throws HttpServerErrorException, JSONException {
        // TODO Auto-generated method stub
        resTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8011/venda-ms/api/vendas";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dataVenda", vendaNova.getDataVenda());
        jsonObject.put("valorTotal", vendaNova.getValorTotal());
        jsonObject.put("produtosVendidos", new Gson().toJson(vendaNova.getProdutosVendidos()));


        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);

        System.out.println(new Gson().toJson(vendaNova.getProdutosVendidos()));

        System.out.println(jsonObject.toString());

        
        VendaDto p = resTemplate.postForObject(apiUrl, request, VendaDto.class);

        /* VendaDto p = resTemplate.exchange(apiUrl, 
            HttpMethod.POST, 
            request, 
            new ParameterizedTypeReference<VendaDto>() {}).getBody(); */

        /* HttpHeaders headers = new HttpHeaders();
        JSONObject jsonObject = new JSONObject();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        

        jsonObject.put("dataVenda", vendaNova.getDataVenda());
        jsonObject.put("valorTotal", 100.0);

        JsonArray jsonArray = (JsonArray) new Gson()
            .toJsonTree(vendaNova.getProdutosVendidos(), new TypeToken<List<Produto>>() {}.getType());
         
        System.out.println(jsonObject.toString());

        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);
        
        VendaDto p = resTemplate.postForObject(apiUrl, request, VendaDto.class); */

        return p;
    }
    
}

/*
    private void createEmployee() {

        Employee newEmployee = new Employee("admin", "admin", "admin@gmail.com");

        RestTemplate restTemplate = new RestTemplate();
        Employee result = restTemplate.postForObject(CREATE_EMPLOYEE_ENDPOINT_URL, newEmployee, Employee.class);

        System.out.println(result);
    }

*/

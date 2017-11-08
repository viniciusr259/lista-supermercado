package br.com.listamercado.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView list_view;
    EditText txt_produo;
    List<Produto> lstProdutos;

    ProdutoAdapter adapter;
    View.OnClickListener click_ck = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            CheckBox ck = (CheckBox) view;

            int posicao = (int) ck.getTag();

            Produto produtoSelect = adapter.getItem(posicao);

            Produto produdoDB = Produto.findById(Produto.class,produtoSelect.getId());

            if (ck.isChecked()){
                produdoDB.setAtivo(true);
                produdoDB.save();
                produtoSelect.setAtivo(true);
                Toast.makeText(MainActivity.this, "checked", Toast.LENGTH_SHORT).show();
            }else{
                produdoDB.setAtivo(false);
                produdoDB.save();

                produtoSelect.setAtivo(false);
                Toast.makeText(MainActivity.this, "no checked", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_view = (ListView) findViewById(R.id.lst_view);
        txt_produo = (EditText) findViewById(R.id.txtProduto);

        lstProdutos = Produto.listAll(Produto.class);

        adapter = new ProdutoAdapter(this, lstProdutos);

        list_view.setAdapter(adapter);
    }
    public void InserirItem(View view) {

        String produto = txt_produo.getText().toString();

        if (produto.isEmpty())return;

        //Criando o obj produto
        Produto p = new Produto(produto,false);

        //adicionando no banco
        p.save();

        //Adicionando na lista
        adapter.add(p);

    }

    //Classe do Adapter
    private class ProdutoAdapter extends ArrayAdapter<Produto>{

        public ProdutoAdapter(Context context, List<Produto> items)
        {
            super(context, 0, items);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View v = convertView;

            if (v == null){
                v = LayoutInflater.from(getContext()).inflate(R.layout.item_lista,null);
            }

            Produto item = getItem(position);

            TextView txt_item_produto = v.findViewById(R.id.txt_item_produto);
            CheckBox ck_item_produto = v.findViewById(R.id.ck_item_produto);

            txt_item_produto.setText(item.getNome());

            ck_item_produto.setChecked(item.isAtivo());
            ck_item_produto.setTag(position);
            ck_item_produto.setOnClickListener(click_ck);



            return v;
        }
    }


}

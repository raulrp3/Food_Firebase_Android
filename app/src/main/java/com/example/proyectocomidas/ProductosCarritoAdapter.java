package com.example.proyectocomidas;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductosCarritoAdapter extends RecyclerView.Adapter<ProductosCarritoAdapter.ViewHolderCestaCarrito> {


    public class ViewHolderCestaCarrito extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView tvName, tvPrecio;
        Button btnDelete;

        public ViewHolderCestaCarrito(View itemView){
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imagenProductoCesta);
            tvName = itemView.findViewById(R.id.nombreProductoCesta);
            tvPrecio = itemView.findViewById(R.id.precioProductoCesta);
            btnDelete = itemView.findViewById(R.id.btnEliminarProductoCesta);
        }
    }

    private Context context;
    private List<Producto> products;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAtuh;
    private SharedPreferences preferences;
    private ProductosCompra productsShop;
    private Double precioTotal;
    private TextView txtPrecioTotal;

    final long ONE_MEGABYTE = 1024 * 1024;

    public ProductosCarritoAdapter(Context context, List<Producto> products, FirebaseStorage mStorage, FirebaseAuth mAuth, Double precioTotal, TextView txtPrecioTotal){
        this.context = context;
        this.products = products;
        this.mStorage = mStorage;
        this.mAtuh = mAuth;
        preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        productsShop = new ProductosCompra();
        this.precioTotal = precioTotal;
        this.txtPrecioTotal = txtPrecioTotal;
    }

    @NonNull
    @Override
    public ProductosCarritoAdapter.ViewHolderCestaCarrito onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_producto_cesta, viewGroup, false);
        final ViewHolderCestaCarrito vhcc = new ViewHolderCestaCarrito(view);

        return vhcc;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductosCarritoAdapter.ViewHolderCestaCarrito viewHolderCestaCarrito, final int i) {

        viewHolderCestaCarrito.tvName.setText(products.get(i).getNombre());
        viewHolderCestaCarrito.tvPrecio.setText(products.get(i).getPrecio() + "€");
        String image = products.get(i).getImagen();
        mStorage.getReference().child(image).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolderCestaCarrito.imgProduct.setImageBitmap(Bitmap.createScaledBitmap(bmp, viewHolderCestaCarrito.imgProduct.getWidth(), viewHolderCestaCarrito.imgProduct.getHeight(), false));
            }
        });

        viewHolderCestaCarrito.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                precioTotal -= products.get(i).getPrecio();
                products.remove(i);
                notifyDataSetChanged();
                txtPrecioTotal.setText(precioTotal + "€");

                if (products.size() >= 0) {
                    productsShop.añadirProductos(products);
                    String json = productsShop.toJson();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("productos", json);
                    editor.putString("observaciones", "");
                    editor.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}

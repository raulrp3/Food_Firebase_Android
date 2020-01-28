package com.example.proyectocomidas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolderProduct> implements Filterable {

    public class ViewHolderProduct extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView tvName;
        TextView tvPrice;
        Button btnAdd;

        public ViewHolderProduct(View itemView){
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imagenProducto);
            tvName = itemView.findViewById(R.id.nombreProducto);
            tvPrice = itemView.findViewById(R.id.precioProducto);
            btnAdd = itemView.findViewById(R.id.btnAñadir);

        }
    }

    private Context context;
    private List<Producto> products;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAtuh;
    private List<Producto> fullProducts;
    private List<Producto> productsAdded;
    private ProductosCompra productsShop;
    private SharedPreferences preferences;
    private CustomClickPedido listener;

    final long ONE_MEGABYTE = 1024 * 1024;



    private Filter productsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Producto> filteredlist = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredlist.addAll(fullProducts);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Producto item : fullProducts) {
                    if (item.getNombre().toLowerCase().contains(filterPattern)) {
                        filteredlist.add(item);
                    }
                }
            }

            FilterResults result = new FilterResults();
            result.values = filteredlist;

            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            products.clear();
            products.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public ProductoAdapter(Context context, List<Producto> products, FirebaseStorage mStorage, FirebaseAuth mAuth, ProductosCompra productsShop, CustomClickPedido listener){
        this.listener = listener;
        this.context = context;
        this.products = products;
        fullProducts = new ArrayList<>(products);
        this.mStorage = mStorage;
        this.mAtuh = mAuth;
        this.productsAdded = new ArrayList<>();
        mAuth.signInAnonymously();
        this.productsShop = productsShop;
        preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_producto, viewGroup, false);
        final ViewHolderProduct vhp = new ViewHolderProduct(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(view, vhp.getAdapterPosition());
            }
        });

        return vhp;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderProduct viewHolderProduct, final int i) {

        viewHolderProduct.tvName.setText(products.get(i).getNombre());
        viewHolderProduct.tvPrice.setText(products.get(i).getPrecio() + "€");

        String image = products.get(i).getImagen();
        mStorage.getReference().child(image).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolderProduct.imgProduct.setImageBitmap(Bitmap.createScaledBitmap(bmp, 525, 525, false));
            }
        });

        viewHolderProduct.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productsAdded.add(products.get(i));
                productsShop.añadirProductos(productsAdded);
                String json = productsShop.toJson();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("productos", json);
                editor.putString("observaciones", "");
                editor.commit();
                Snackbar snackbar = Snackbar.make(viewHolderProduct.itemView, "¡Producto añadido con éxito!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });


        // Si un producto no esta disponible, deshabilito el boton de añadir, cambio el color al nombre y dejo la imagen tal cual.

        if(!products.get(i).getDisponible()){

            //viewHolderProduct.tvName.setEnabled(false);

            viewHolderProduct.tvName.setTextColor(Color.parseColor("#F44336"));

            viewHolderProduct.btnAdd.setEnabled(false);

            viewHolderProduct.btnAdd.setBackgroundColor(Color.parseColor("#979797"));

        }else {

            if (!viewHolderProduct.btnAdd.isEnabled()) {
                viewHolderProduct.btnAdd.setEnabled(true);
                viewHolderProduct.tvName.setTextColor(Color.parseColor("#000000"));
                viewHolderProduct.btnAdd.setBackgroundColor(Color.parseColor("#423D3D"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Producto> products){
        this.products = products;
    }

    @Override
    public Filter getFilter() {
        return productsFilter;
    }

    public void setProductsAdded(List<Producto> productsAdded){ this.productsAdded = productsAdded; }
}

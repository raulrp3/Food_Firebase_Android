package com.example.proyectocomidas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class DetallePedidoAdapter extends RecyclerView.Adapter<DetallePedidoAdapter.ViewHolderDetallePedido> {

    public class ViewHolderDetallePedido extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView tvNameProduct;

        public ViewHolderDetallePedido(View view){
            super(view);

            imgProduct = view.findViewById(R.id.imagenDetalle);
            tvNameProduct = view.findViewById(R.id.nombreDetalle);
        }
    }

    private Context context;
    private List<Producto> products;
    private FirebaseStorage mStorage;

    final long ONE_MEGABYTE = 1024 * 1024;

    public DetallePedidoAdapter(Context context, List<Producto> products) {
        this.context = context;
        this.products = products;
        mStorage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ViewHolderDetallePedido onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_detalle_pedido, viewGroup, false);
        final ViewHolderDetallePedido vhdp = new ViewHolderDetallePedido(view);

        return vhdp;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDetallePedido viewHolderDetallePedido, int i) {
        viewHolderDetallePedido.tvNameProduct.setText(products.get(i).getNombre());

        String image = products.get(i).getImagen();
        mStorage.getReference().child(image).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolderDetallePedido.imgProduct.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}

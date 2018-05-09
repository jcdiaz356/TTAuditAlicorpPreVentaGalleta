package com.dataservicios.ttauditalicorppreventagalleta.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.model.Distributor;
import com.dataservicios.ttauditalicorppreventagalleta.model.OrderDetailTemp;
import com.dataservicios.ttauditalicorppreventagalleta.model.Product;
import com.dataservicios.ttauditalicorppreventagalleta.repo.DistributorRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.OrderDetailTempRepo;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by jcdia on 7/06/2017.
 */

public class ProductAdapterRecyclerView extends RecyclerView.Adapter<ProductAdapterRecyclerView.ProductViewHolder> {
    private ArrayList<Product>          products;
    private int                         resource;
    private Activity                    activity;
    private int                         store_id;
    private int                         audit_id;
    private Product                     product;
    private OrderDetailTemp             orderDetailTemp;
    private OrderDetailTempRepo         orderDetailTempRepo;
    private OnEditTextChanged           onEditTextChanged;

    public ProductAdapterRecyclerView(ArrayList<Product> products, int resource, Activity activity, int store_id, int audit_id, int distributor_id,OnEditTextChanged onEditTextChanged) {
        this.products = products;
        this.resource           = resource;
        this.activity           = activity;
        this.store_id           = store_id;
        this.audit_id           = audit_id;
        orderDetailTempRepo     = new OrderDetailTempRepo(activity);
        this.onEditTextChanged = onEditTextChanged;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        product = products.get(position);
        holder.tvProduct.setText(product.getFullname());
        holder.tvCodigo.setText(product.getCode());
        holder.etCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cantidad = s.toString().trim();

                if(cantidad.length() > 0) {
                    product             = products.get(position);
                    orderDetailTemp     = (OrderDetailTemp) orderDetailTempRepo.findByProductId(product.getId());

                    String price = holder.etPrecio.getText().toString().trim() ;
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    if (price.length() > 0) {
                        float resul = Float.valueOf(price) * Float.valueOf(cantidad);
                        float twoDigitsF = Float.valueOf(decimalFormat.format(resul));
                        holder.tvTotal.setText("S/. " + String.valueOf(twoDigitsF));


                        orderDetailTemp.setQuantity(Integer.valueOf(cantidad));
                        orderDetailTemp.setTotal(twoDigitsF);
                        orderDetailTemp.setPrice(Float.valueOf(price));
                        orderDetailTempRepo.update(orderDetailTemp);


                        ArrayList<OrderDetailTemp> orderDetailTemps = (ArrayList<OrderDetailTemp>) orderDetailTempRepo.findAll();
                        float mountTotal=0;
                        for(OrderDetailTemp m: orderDetailTemps){
                            if(m.getQuantity() > 0){
                                mountTotal += m.getTotal();
                            }
                        }

                        twoDigitsF = Float.valueOf(decimalFormat.format(mountTotal));
                        onEditTextChanged.onTextChanged(position, String.valueOf(twoDigitsF));
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProduct;
        private TextView tvCodigo;
        private EditText etPrecio;
        private TextView tvTotal;
        private EditText etCantidad;


        public ProductViewHolder(View itemView) {
            super(itemView);
            tvProduct       = (TextView) itemView.findViewById(R.id.tvProduct);
            tvCodigo        = (TextView) itemView.findViewById(R.id.tvCodigo);
            etPrecio        = (EditText)  itemView.findViewById(R.id.etPrecio);
            tvTotal         = (TextView)  itemView.findViewById(R.id.tvTotal);
            etCantidad      = (EditText)  itemView.findViewById(R.id.etCantidad);

        }
    }


    public void setFilter(ArrayList<Product> products){
        this.products = new ArrayList<>();
        this.products.addAll(products);
        notifyDataSetChanged();
    }


}

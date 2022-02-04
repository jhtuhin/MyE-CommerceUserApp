package com.example.mye_commerceuserapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mye_commerceuserapp.callbacks.AddRemoveCartItemListener;
import com.example.mye_commerceuserapp.callbacks.OnProductItemClickListener;
import com.example.mye_commerceuserapp.databinding.ProductListRowBinding;
import com.example.mye_commerceuserapp.models.CartModel;
import com.example.mye_commerceuserapp.models.ProductsModel;
import com.example.mye_commerceuserapp.models.UserProductModel;


public class ProductListAdapter  extends ListAdapter<UserProductModel, ProductListAdapter.ProductListViewHolder> {
    private OnProductItemClickListener listener;
    private AddRemoveCartItemListener cartItemListener;
    private ProductListRowBinding binding;

    public ProductListAdapter(OnProductItemClickListener listener, AddRemoveCartItemListener cartItemListener){
        super(new ProductDiff());
        this.listener = listener;
        this.cartItemListener = cartItemListener;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=ProductListRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ProductListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        final UserProductModel userProductModel = getItem(position);
        holder.bind(userProductModel);
        if (getItem(position).isInCart()) {
            holder.binding.addToCartBtn.setVisibility(View.GONE);
            holder.binding.removeFromCartBtn.setVisibility(View.VISIBLE);
        }else {
            holder.binding.addToCartBtn.setVisibility(View.VISIBLE);
            holder.binding.removeFromCartBtn.setVisibility(View.GONE);
        }

        holder.binding.addToCartBtn.setOnClickListener(v -> {
            userProductModel.setInCart(true);
            //notifyItemChanged(position, userProductModel);
            final CartModel cartModel = new CartModel(
                    userProductModel.getProductID(),
                    userProductModel.getProductName(),
                    userProductModel.getPrice(),
                    1
            );
            cartItemListener.onCartItemAdd(cartModel, position);
        });
        holder.binding.removeFromCartBtn.setOnClickListener(v -> {
            userProductModel.setInCart(false);
            //notifyItemChanged(position, userProductModel);
            cartItemListener.onCartItemRemove(userProductModel.getProductID(), position);
        });

    }


    public class ProductListViewHolder extends RecyclerView.ViewHolder{
        private ProductListRowBinding binding;
        public ProductListViewHolder(ProductListRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.productListCardView.setOnClickListener(v -> {
                listener.onProductItemClicked(
                        getItem(getAdapterPosition()).getProductID());
            });
        }

        public void bind(UserProductModel item) {
            binding.setProduct(item);
        }
    }

    static class ProductDiff extends DiffUtil.ItemCallback<UserProductModel>{

        @Override
        public boolean areItemsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductID().equals(newItem.getProductID());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductID().equals(newItem.getProductID());
        }
    }


}

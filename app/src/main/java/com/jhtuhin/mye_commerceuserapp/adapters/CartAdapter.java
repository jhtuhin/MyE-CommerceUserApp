package com.jhtuhin.mye_commerceuserapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jhtuhin.mye_commerceuserapp.callbacks.OnCartItemQuantityChangeListener;
import com.jhtuhin.mye_commerceuserapp.databinding.CartItemRowBinding;
import com.jhtuhin.mye_commerceuserapp.models.CartModel;


public class CartAdapter extends ListAdapter<CartModel, CartAdapter.ProductViewHolder> {
    private OnCartItemQuantityChangeListener quantityChangeListener;
    public CartAdapter(OnCartItemQuantityChangeListener QuantityChangeListener) {
        super(new ProductDiff());
        this.quantityChangeListener=QuantityChangeListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final CartItemRowBinding binding = CartItemRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.binding.cartRowPlusBtn.setOnClickListener(v -> {
            getCurrentList().get(position).setProductQuantity(
                    getCurrentList().get(position).getProductQuantity() + 1);
            holder.binding.cartRowQtyTV.setText(String.valueOf(
                    getCurrentList().get(position).getProductQuantity()));
            quantityChangeListener.onCartItemQuantityChange(getCurrentList());
        });
        holder.binding.cartRowMinusBtn.setOnClickListener(v -> {
            if (getCurrentList().get(position).getProductQuantity() > 1) {
                getCurrentList().get(position).setProductQuantity(
                        getCurrentList().get(position).getProductQuantity() - 1);
                holder.binding.cartRowQtyTV.setText(String.valueOf(
                        getCurrentList().get(position).getProductQuantity()));
                quantityChangeListener.onCartItemQuantityChange(getCurrentList());
            }
        });
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private CartItemRowBinding binding;
        public ProductViewHolder(CartItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(CartModel cartModel) {
            binding.setCartModel(cartModel);
        }
    }

    static class ProductDiff extends DiffUtil.ItemCallback<CartModel>{
        @Override
        public boolean areItemsTheSame(@NonNull CartModel oldItem, @NonNull CartModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartModel oldItem, @NonNull CartModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }
    }
}

import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getCartList, addToCart, updateCartQuantity, removeFromCart, clearCart } from '@/api/client/cart';

export const useCartStore = defineStore('cart', () => {
  const items = ref([]);
  const loading = ref(false);

  async function fetchCart() {
    loading.value = true;
    try {
      const res = await getCartList();
      items.value = res.data || [];
    } finally {
      loading.value = false;
    }
  }

  async function add(bookId, quantity) {
    await addToCart(bookId, quantity);
    await fetchCart();
  }

  async function updateQuantity(id, quantity) {
    await updateCartQuantity(id, quantity);
    await fetchCart();
  }

  async function remove(id) {
    await removeFromCart(id);
    await fetchCart();
  }

  async function clearSelected() {
    await clearCart();
    await fetchCart();
  }

  return { items, loading, fetchCart, add, updateQuantity, remove, clearSelected };
});
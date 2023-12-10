import 'package:flutter/material.dart';

class CataloguePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('APAPEDIA'), //ganti logo
        actions: [
          IconButton(
            icon: Icon(Icons.shopping_cart),
            onPressed: () {
              // Aksi untuk menavigasi ke keranjang belanja
            },
          ),
        ],
      ),
      body: ListView(
        children: [
          // Banner promosi atau gambar header
          Container(
            height: 200,
            color: Colors.grey[300],
            // Tambahkan widget lainnya seperti gambar, teks, dll.
          ),
          // Daftar kategori produk
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Text(
              'Categories',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
          ),
          Container(
            height: 100,
            // Tambahkan widget daftar kategori (mungkin menggunakan ListView.builder)
          ),
          // Produk terpopuler
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Text(
              'Popular Products',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
          ),
          Container(
            height: 200,
            // Tambahkan widget daftar produk (mungkin menggunakan ListView.builder)
          ),
        ],
      ),
    );
  }
}

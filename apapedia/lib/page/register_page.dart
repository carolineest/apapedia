import 'package:flutter/material.dart';
import 'package:dio/dio.dart';
import 'package:apapedia/utils/color_pallete.dart';

class RegistrationPage extends StatefulWidget {
  @override
  _RegistrationPageState createState() => _RegistrationPageState();
}

class _RegistrationPageState extends State<RegistrationPage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  TextEditingController _nameController = TextEditingController();
  TextEditingController _usernameController = TextEditingController();
  TextEditingController _passwordController = TextEditingController();
  TextEditingController _emailController = TextEditingController();
  TextEditingController _addressController = TextEditingController();

  Future<bool> _handleRegistration() async {
    try {
      print('run');
      print(_usernameController.text);
      print(_passwordController.text);
      Map<String, dynamic> data = {
        'name': _nameController.text,
        'username': _usernameController.text,
        'password': _passwordController.text,
        'email': _emailController.text,
        'role': 'Customer',
        'address': _addressController.text,
      };

      Response response = await Dio().post(
        'http://localhost:8082/api/auth/register',
        data: data,
      );

      print(response.data);
      print(response.data['id']);
      print(response.data['username']);

      return true;
    } catch (error) {
      print(error);
      return false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Container(
        color: Colors.white, // Ganti warna latar belakang sesuai keinginan Anda
        child: Center(
          child: Padding(
            padding: EdgeInsets.all(16.0),
            child: Card(
              color: Colors.white, // Ganti warna card sesuai keinginan Anda
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8.0),
              ),
              elevation: 5.0,
              child: Padding(
                padding: EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  mainAxisSize: MainAxisSize.min, // Sesuaikan ukuran card dengan content
                  children: <Widget>[
                    Text(
                      'Sign Up',
                      style: TextStyle(
                        fontSize: 20.0,
                        fontFamily: 'Poppins',
                        fontWeight: FontWeight.bold,
                        color: Colors.grey[800],
                      ),
                      textAlign: TextAlign.center,
                    ),
                    SizedBox(height: 20.0),
                    TextField(
                      controller: _nameController,
                      decoration: InputDecoration(
                        labelText: 'Name',
                        prefixIcon: Icon(Icons.person),
                        filled: true,
                        fillColor: Colors.white, // Ganti warna latar belakang text field sesuai keinginan Anda
                        hoverColor: Colors.white,
                        focusedBorder: OutlineInputBorder(
                          borderSide: BorderSide(color: Colors.blue, width: 2.0),
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                      ),
                    ),
                    SizedBox(height: 12.0),
                    TextField(
                      controller: _usernameController,
                      decoration: InputDecoration(
                        labelText: 'Username',
                        prefixIcon: Icon(Icons.person),
                        filled: true,
                        fillColor: Colors.white, // Ganti warna latar belakang text field sesuai keinginan Anda
                        hoverColor: Colors.white,
                        focusedBorder: OutlineInputBorder(
                          borderSide: BorderSide(color: Colors.blue, width: 2.0),
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                      ),
                    ),
                    SizedBox(height: 12.0),
                    TextField(
                      controller: _passwordController,
                      obscureText: true,
                      decoration: InputDecoration(
                        labelText: 'Password',
                        prefixIcon: Icon(Icons.lock),
                        filled: true,
                        fillColor: Colors.white, // Ganti warna latar belakang text field sesuai keinginan Anda
                        hoverColor: Colors.white,
                        focusedBorder: OutlineInputBorder(
                          borderSide: BorderSide(color: Colors.blue, width: 2.0),
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                      ),
                    ),
                    SizedBox(height: 12.0),
                    TextField(
                      controller: _emailController,
                      keyboardType: TextInputType.emailAddress,
                      decoration: InputDecoration(
                        labelText: 'Email',
                        prefixIcon: Icon(Icons.email),
                        filled: true,
                        fillColor: Colors.white, // Ganti warna latar belakang text field sesuai keinginan Anda
                        hoverColor: Colors.white,
                        focusedBorder: OutlineInputBorder(
                          borderSide: BorderSide(color: Colors.blue, width: 2.0),
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                      ),
                    ),
                    SizedBox(height: 12.0),
                    TextField(
                      controller: _addressController,
                      decoration: InputDecoration(
                        labelText: 'Address',
                        prefixIcon: Icon(Icons.location_on),
                        filled: true,
                        fillColor: Colors.white, // Ganti warna latar belakang text field sesuai keinginan Anda
                        hoverColor: Colors.white,
                        focusedBorder: OutlineInputBorder(
                          borderSide: BorderSide(color: Colors.blue, width: 2.0),
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                      ),
                    ),
                    SizedBox(height: 24.0),
                    ElevatedButton(
                      onPressed: () async {
                        var success = await _handleRegistration();
                          if (success) {
                            // ignore: use_build_context_synchronously
                            // Navigator.push(
                            //   context,
                            //   MaterialPageRoute(
                            //     builder: (context) =>
                            //       const BukuScreen(),
                            //   ),
                            // );
                            print("BERHASIL REGISTER !");
                          }
                      },
                      style: ElevatedButton.styleFrom(
                        primary: Colors.blue,
                        onPrimary: Colors.white,
                        elevation: 5.0,
                        padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 12.0),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                      ),
                      child: Text('Save'),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

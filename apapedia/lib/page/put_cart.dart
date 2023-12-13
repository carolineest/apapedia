import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class PutCartPage extends StatefulWidget {
    @override
  _PutCartState createState() => _PutCartState();
}

class _PutCartState extends State<PutCartPage> {

  TextEditingController _newQty = TextEditingController();

  Future<bool> _handleUpdate() async {
    try {
      print('run');
      print(_newQty.text);
      Map<String, dynamic> data = {
        'quantity': _newQty.text,
      };

      Response response = await Dio().post(
        'http://localhost:8081/api/cartitem/update/75d29536-75b4-48ce-8af2-6f70d5d800ed',
        data: data,
      );

      print(response.data);

      return true;
    } catch (error) {
      print(error);
      return false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: Colors.white,
        automaticallyImplyLeading: false,
        title: Row(
          mainAxisSize: MainAxisSize.max,
          children: [
            IconButton(
              icon: Icon(
                Icons.arrow_back_rounded,
                color: Color(0xFF14181B),
                size: 30,
              ),
              onPressed: () {
                Navigator.pop(context);
              },
            ),
            Text(
              'UPDATE QUANTITY ORDER ITEIM',
              style: TextStyle(
                fontFamily: 'Plus Jakarta Sans',
                color: Color(0xFF14181B),
                fontSize: 16,
                fontWeight: FontWeight.normal,
              ),
            ),
          ],
        ),
        actions: [],
        centerTitle: false,
        elevation: 0,
      ),
      body: SafeArea(
        top: true,
        child: Column(
          mainAxisSize: MainAxisSize.max,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Padding(
              padding: EdgeInsets.all(16.0),
              child: Text(
                'Top Up APAPAY',
                style: TextStyle(
                  fontFamily: 'Plus Jakarta Sans',
                  color: Color(0xFF14181B),
                  fontSize: 24,
                  fontWeight: FontWeight.normal,
                ),
              ),
            ),
            Padding(
              padding: EdgeInsets.all(16.0),
              child: TextFormField(
                controller: _newQty,
                keyboardType: TextInputType.number,
                inputFormatters: <TextInputFormatter>[
                  FilteringTextInputFormatter.digitsOnly
                ],
                decoration: InputDecoration(
                  labelText: 'Enter Amount',
                  labelStyle: TextStyle(
                    fontFamily: 'Plus Jakarta Sans',
                    color: Color(0xFF57636C),
                    fontSize: 14,
                    fontWeight: FontWeight.normal,
                  ),
                  enabledBorder: OutlineInputBorder(
                    borderSide: BorderSide(
                      color: Color(0xFFE0E3E7),
                      width: 2,
                    ),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(
                      color: Color(0xFF4B39EF),
                      width: 2,
                    ),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  filled: true,
                  fillColor: Colors.white,
                ),
              ),
            ),
            Padding(
              padding: EdgeInsets.all(16.0),
              child: ElevatedButton(
                onPressed: () async {
                    var success = await _handleUpdate();
                    if (success) {
                    // ignore: use_build_context_synchronously
                        // Navigator.push(
                        //     context,
                        //     MaterialPageRoute(
                        //         builder: (context) =>
                        //             const BukuScreen(),
                        //     ),
                        // );
                    print("UPDATE QUANTITY BERHASIL 1");
                    }
                },
                style: ElevatedButton.styleFrom(
                  primary: Color(0xFF4B39EF),
                  padding: EdgeInsets.symmetric(vertical: 16),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                ),
                child: Text(
                  'Confirm',
                  style: TextStyle(
                    fontFamily: 'Lexend Deca',
                    color: Colors.white,
                    fontSize: 16,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
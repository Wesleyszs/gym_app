import 'package:flutter/material.dart';
import 'package:gym_app/screens/login_screen.dart';
import 'package:gym_app/screens/register_screen.dart';
import 'package:gym_app/screens/home_screen.dart'; // Adicione a importação da tela Home

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Gym App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: LoginScreen(),
      routes: {
        '/register': (context) => RegisterScreen(),
        '/home': (context) => HomeScreen(), // Adicione a rota para a tela Home
      },
    );
  }
}

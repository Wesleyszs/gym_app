import 'package:flutter/material.dart';
import 'package:gym_app/screens/login_screen.dart';
import 'package:gym_app/screens/register_screen.dart';
import 'package:gym_app/screens/home_screen.dart';
import 'package:gym_app/screens/crud_screen.dart';
import 'package:gym_app/screens/cadastro_paciente_screen.dart';

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
        '/home': (context) => HomeScreen(),
        '/crud': (context) => CrudScreen(),
        '/cadastro_paciente': (context) => CadastroPacienteScreen(),
      },
    );
  }
}

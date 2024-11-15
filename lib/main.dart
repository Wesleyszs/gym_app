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
  final int profissionalId = 1;

  const MyApp({Key? key}) : super(key: key); // Defina o ID do profissional logado aqui

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
        '/home': (context) => HomeScreen(profissionalId: profissionalId),
        '/crud': (context) => CrudScreen(profissionalId: profissionalId),
        '/cadastro_paciente': (context) => CadastroPacienteScreen(profissionalId: profissionalId),
      },
    );
  }
}

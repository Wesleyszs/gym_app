import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:gym_app/screens/home_screen.dart'; // Importação da tela de Home

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController senhaController = TextEditingController();

  Future<void> login() async {
    final response = await http.post(
      Uri.parse('http://10.0.2.2:5000/login'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'email': emailController.text,
        'senha': senhaController.text,
      }),
    );

    final data = json.decode(response.body);

    if (response.statusCode == 200) {
      final profissionalId = data['id']; // Supondo que o ID do profissional seja retornado no login

      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text(data['message']),
      ));
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => HomeScreen(profissionalId: profissionalId)),  // Navega para a HomeScreen com o ID do profissional
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text(data['message']),
      ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('GYMLAB ACADEMY'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text(
              'FAÇA SEU LOGIN',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: emailController,
              decoration: InputDecoration(
                labelText: 'Digite seu E-mail',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: senhaController,
              decoration: InputDecoration(
                labelText: 'Senha',
                border: OutlineInputBorder(),
              ),
              obscureText: true,
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: login,
              child: Text('Acessar'),
            ),
            TextButton(
              onPressed: () {
                // Lógica para esquecer senha
              },
              child: Text('Esqueceu sua senha? Clique aqui'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pushNamed(context, '/register');
              },
              child: Text('Não tem uma conta? Cadastre-se'),
            ),
          ],
        ),
      ),
    );
  }
}

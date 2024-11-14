import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class RegisterScreen extends StatefulWidget {
  @override
  _RegisterScreenState createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final TextEditingController nomeController = TextEditingController();
  final TextEditingController crefController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController senhaController = TextEditingController();
  final TextEditingController confirmacaoSenhaController = TextEditingController();
  bool concordaTermos = false;

  Future<void> register() async {
    if (!concordaTermos) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Você deve concordar com os termos para se registrar.'),
      ));
      return;
    }

    if (senhaController.text != confirmacaoSenhaController.text) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('As senhas não coincidem.'),
      ));
      return;
    }

    final response = await http.post(
      Uri.parse('http://10.0.2.2:5000/register'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'nome_completo': nomeController.text,
        'matricula_cref': crefController.text,
        'email': emailController.text,
        'senha': senhaController.text,
      }),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Profissional registrado com sucesso!'),
      ));
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Erro ao registrar!'),
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
              'CADASTRO',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: nomeController,
              decoration: InputDecoration(
                labelText: 'Nome completo',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: crefController,
              decoration: InputDecoration(
                labelText: 'MATRICULA CREF',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: emailController,
              decoration: InputDecoration(
                labelText: 'E-mail',
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
            TextField(
              controller: confirmacaoSenhaController,
              decoration: InputDecoration(
                labelText: 'Confirmação de senha',
                border: OutlineInputBorder(),
              ),
              obscureText: true,
            ),
            SizedBox(height: 20),
            Row(
              children: [
                Checkbox(
                  value: concordaTermos,
                  onChanged: (value) {
                    setState(() {
                      concordaTermos = value ?? false;
                    });
                  },
                ),
                Expanded(
                  child: Text('Eu concordo com os termos'),
                ),
              ],
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: register,
              child: Text('Registrar'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: Text('já tem uma conta? entrar'),
            ),
          ],
        ),
      ),
    );
  }
}

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({Key? key}) : super(key: key);

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
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text('Você deve concordar com os termos para se registrar.'),
      ));
      return;
    }

    if (senhaController.text != confirmacaoSenhaController.text) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
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
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text('Profissional registrado com sucesso!'),
      ));
    } else {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text('Erro ao registrar!'),
      ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('GYMLAB ACADEMY'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const Text(
              'CADASTRO',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: nomeController,
              decoration: const InputDecoration(
                labelText: 'Nome completo',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: crefController,
              decoration: const InputDecoration(
                labelText: 'MATRICULA CREF',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: emailController,
              decoration: const InputDecoration(
                labelText: 'E-mail',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: senhaController,
              decoration: const InputDecoration(
                labelText: 'Senha',
                border: OutlineInputBorder(),
              ),
              obscureText: true,
            ),
            const SizedBox(height: 20),
            TextField(
              controller: confirmacaoSenhaController,
              decoration: const InputDecoration(
                labelText: 'Confirmação de senha',
                border: OutlineInputBorder(),
              ),
              obscureText: true,
            ),
            const SizedBox(height: 20),
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
                const Expanded(
                  child: Text('Eu concordo com os termos'),
                ),
              ],
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: register,
              child: const Text('Registrar'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: const Text('já tem uma conta? entrar'),
            ),
          ],
        ),
      ),
    );
  }
}

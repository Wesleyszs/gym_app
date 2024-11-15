import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class CadastroPacienteScreen extends StatefulWidget {
  @override
  _CadastroPacienteScreenState createState() => _CadastroPacienteScreenState();
}

class _CadastroPacienteScreenState extends State<CadastroPacienteScreen> {
  final TextEditingController nomeController = TextEditingController();
  final TextEditingController idadeController = TextEditingController();
  final TextEditingController pesoController = TextEditingController();
  final TextEditingController alturaController = TextEditingController();
  final TextEditingController condicoesController = TextEditingController();
  final TextEditingController lesoesController = TextEditingController();
  final TextEditingController experienciaController = TextEditingController();
  final TextEditingController frequenciaController = TextEditingController();
  final TextEditingController objetivoController = TextEditingController();
  final TextEditingController fumanteController = TextEditingController();
  final TextEditingController sonoController = TextEditingController();

  Future<void> salvarCadastro() async {
    final response = await http.post(
      Uri.parse('http://10.0.2.2:5000/register_student'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'nome': nomeController.text,
        'idade': idadeController.text,
        'peso': pesoController.text,
        'altura': alturaController.text,
        'condicoes': condicoesController.text,
        'lesoes': lesoesController.text,
        'experiencia': experienciaController.text,
        'frequencia': frequenciaController.text,
        'objetivo': objetivoController.text,
        'fumante': fumanteController.text,
        'sono': sonoController.text,
      }),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Aluno cadastrado com sucesso!'),
      ));
      Navigator.pop(context);  // Volta para a tela anterior
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Erro ao cadastrar aluno!'),
      ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Cadastro de Aluno'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: ListView(
          children: [
            TextField(
              controller: nomeController,
              decoration: InputDecoration(
                labelText: 'Nome Completo',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: idadeController,
              decoration: InputDecoration(
                labelText: 'Idade',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: pesoController,
              decoration: InputDecoration(
                labelText: 'Peso',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: alturaController,
              decoration: InputDecoration(
                labelText: 'Altura',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: condicoesController,
              decoration: InputDecoration(
                labelText: 'Condições Médicas/Cirurgias',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: lesoesController,
              decoration: InputDecoration(
                labelText: 'Lesões/Problemas Musculares',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: experienciaController,
              decoration: InputDecoration(
                labelText: 'Nível de Experiência',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: frequenciaController,
              decoration: InputDecoration(
                labelText: 'Frequência de Treino Semanal',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: objetivoController,
              decoration: InputDecoration(
                labelText: 'Objetivo com o Treino',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: fumanteController,
              decoration: InputDecoration(
                labelText: 'Você fuma?',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            TextField(
              controller: sonoController,
              decoration: InputDecoration(
                labelText: 'Horas de Sono por Noite',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: salvarCadastro,
              child: Text('Salvar'),
            ),
          ],
        ),
      ),
    );
  }
}

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class CadastroPacienteScreen extends StatefulWidget {
  final int profissionalId; // Adicione o profissionalId como argumento

  const CadastroPacienteScreen({Key? key, required this.profissionalId}) : super(key: key);

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
        'profissional_id': widget.profissionalId, // Adicione o profissionalId aqui
      }),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text('Aluno cadastrado com sucesso!'),
      ));
      Navigator.pop(context);  // Volta para a tela anterior
    } else {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text('Erro ao cadastrar aluno!'),
      ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Cadastro de Aluno'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: ListView(
          children: [
            TextField(
              controller: nomeController,
              decoration: const InputDecoration(
                labelText: 'Nome Completo',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: idadeController,
              decoration: const InputDecoration(
                labelText: 'Idade',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: pesoController,
              decoration: const InputDecoration(
                labelText: 'Peso',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: alturaController,
              decoration: const InputDecoration(
                labelText: 'Altura',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: condicoesController,
              decoration: const InputDecoration(
                labelText: 'Condições Médicas/Cirurgias',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: lesoesController,
              decoration: const InputDecoration(
                labelText: 'Lesões/Problemas Musculares',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: experienciaController,
              decoration: const InputDecoration(
                labelText: 'Nível de Experiência',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: frequenciaController,
              decoration: const InputDecoration(
                labelText: 'Frequência de Treino Semanal',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: objetivoController,
              decoration: const InputDecoration(
                labelText: 'Objetivo com o Treino',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: fumanteController,
              decoration: const InputDecoration(
                labelText: 'Você fuma?',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            TextField(
              controller: sonoController,
              decoration: const InputDecoration(
                labelText: 'Horas de Sono por Noite',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: salvarCadastro,
              child: const Text('Salvar'),
            ),
          ],
        ),
      ),
    );
  }
}

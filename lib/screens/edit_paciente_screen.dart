import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class EditPacienteScreen extends StatefulWidget {
  final Map<String, dynamic> aluno;

  EditPacienteScreen({required this.aluno});

  @override
  _EditPacienteScreenState createState() => _EditPacienteScreenState();
}

class _EditPacienteScreenState extends State<EditPacienteScreen> {
  late TextEditingController nomeController;
  late TextEditingController idadeController;
  late TextEditingController pesoController;
  late TextEditingController alturaController;
  late TextEditingController condicoesController;
  late TextEditingController lesoesController;
  late TextEditingController experienciaController;
  late TextEditingController frequenciaController;
  late TextEditingController objetivoController;
  late TextEditingController fumanteController;
  late TextEditingController sonoController;

  @override
  void initState() {
    super.initState();
    nomeController = TextEditingController(text: widget.aluno['nome']);
    idadeController = TextEditingController(text: widget.aluno['idade'].toString());
    pesoController = TextEditingController(text: widget.aluno['peso'].toString());
    alturaController = TextEditingController(text: widget.aluno['altura'].toString());
    condicoesController = TextEditingController(text: widget.aluno['condicoes']);
    lesoesController = TextEditingController(text: widget.aluno['lesoes']);
    experienciaController = TextEditingController(text: widget.aluno['experiencia']);
    frequenciaController = TextEditingController(text: widget.aluno['frequencia']);
    objetivoController = TextEditingController(text: widget.aluno['objetivo']);
    fumanteController = TextEditingController(text: widget.aluno['fumante']);
    sonoController = TextEditingController(text: widget.aluno['sono']);
  }

  Future<void> salvarEdicao() async {
    final response = await http.put(
      Uri.parse('http://10.0.2.2:5000/update_student/${widget.aluno['id']}'),
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
        content: Text('Aluno atualizado com sucesso!'),
      ));
      Navigator.pop(context); // Volta para a tela anterior
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Erro ao atualizar aluno!'),
      ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Editar Aluno'),
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
              onPressed: salvarEdicao,
              child: Text('Salvar'),
            ),
          ],
        ),
      ),
    );
  }
}

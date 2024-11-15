
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';
import 'package:image_picker/image_picker.dart';

class ExercicioScreen extends StatefulWidget {
  final int profissionalId;

  ExercicioScreen({required this.profissionalId});

  @override
  _ExercicioScreenState createState() => _ExercicioScreenState();
}

class _ExercicioScreenState extends State<ExercicioScreen> {
  Map<String, List<Map<String, dynamic>>> _exerciciosPorDia = {
    'Segunda-feira': [],
    'Terça-feira': [],
    'Quarta-feira': [],
    'Quinta-feira': [],
    'Sexta-feira': [],
    'Sábado': [],
    'Domingo': []
  };

  final picker = ImagePicker();

  Future<void> addExercicio(String diaDaSemana, Map<String, dynamic> exercicio) async {
    if (exercicio['video'] != null) {
      final request = http.MultipartRequest(
        'POST',
        Uri.parse('http://10.0.2.2:5000/upload_video'), // Endereço do endpoint para upload do vídeo
      );
      request.files.add(
        await http.MultipartFile.fromPath(
          'video',
          exercicio['video'].path,
        ),
      );

      final response = await request.send();

      if (response.statusCode == 200) {
        final responseBody = await response.stream.bytesToString();
        final jsonResponse = json.decode(responseBody);
        exercicio['video_url'] = jsonResponse['video_url'];
      } else {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text('Erro ao fazer upload do vídeo!'),
        ));
        return;
      }
    }

    exercicio['profissional_id'] = widget.profissionalId;
    exercicio['dia_da_semana'] = diaDaSemana;

    final response = await http.post(
      Uri.parse('http://10.0.2.2:5000/add_exercise'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode(exercicio),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Exercício adicionado com sucesso!'),
      ));
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Erro ao adicionar exercício!'),
      ));
    }
  }

  Future<void> pickVideo(String diaDaSemana, int index) async {
    final pickedFile = await picker.pickVideo(source: ImageSource.gallery);
    if (pickedFile != null) {
      setState(() {
        _exerciciosPorDia[diaDaSemana]![index]['video'] = File(pickedFile.path);
      });
    }
  }

  void addNovoExercicio(String diaDaSemana) {
    setState(() {
      _exerciciosPorDia[diaDaSemana]!.add({
        'parte_do_corpo': '',
        'nome_exercicio': '',
        'video': null,
        'series': 0,
        'peso': 0.0,
        'descanso': 0,
      });
    });
  }

  Future<void> salvarTodosExercicios() async {
    for (String dia in _exerciciosPorDia.keys) {
      for (var exercicio in _exerciciosPorDia[dia]!) {
        await addExercicio(dia, exercicio);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Adicionar Exercícios'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Expanded(
              child: ListView(
                children: _exerciciosPorDia.keys.map((String dia) {
                  return ExpansionTile(
                    title: Text(dia),
                    children: [
                      ..._exerciciosPorDia[dia]!.map((exercicio) {
                        int index = _exerciciosPorDia[dia]!.indexOf(exercicio);
                        return Card(
                          margin: EdgeInsets.symmetric(vertical: 10),
                          child: Padding(
                            padding: const EdgeInsets.all(16.0),
                            child: Column(
                              children: [
                                TextField(
                                  decoration: InputDecoration(
                                    labelText: 'Parte do Corpo',
                                    border: OutlineInputBorder(),
                                  ),
                                  onChanged: (value) {
                                    setState(() {
                                      exercicio['parte_do_corpo'] = value;
                                    });
                                  },
                                ),
                                SizedBox(height: 20),
                                TextField(
                                  decoration: InputDecoration(
                                    labelText: 'Nome do Exercício',
                                    border: OutlineInputBorder(),
                                  ),
                                  onChanged: (value) {
                                    setState(() {
                                      exercicio['nome_exercicio'] = value;
                                    });
                                  },
                                ),
                                SizedBox(height: 20),
                                ElevatedButton(
                                  onPressed: () => pickVideo(dia, index),
                                  child: Text('Selecionar Vídeo'),
                                ),
                                if (exercicio['video'] != null) ...[
                                  SizedBox(height: 10),
                                  Text('Vídeo selecionado: ${exercicio['video'].path.split('/').last}')
                                ],
                                SizedBox(height: 20),
                                TextField(
                                  decoration: InputDecoration(
                                    labelText: 'Séries',
                                    border: OutlineInputBorder(),
                                  ),keyboardType: TextInputType.number,
                                  onChanged: (value) {
                                    setState(() {
                                      exercicio['series'] = int.parse(value);
                                    });
                                  },
                                ),
                                SizedBox(height: 20),
                                TextField(
                                  decoration: InputDecoration(
                                    labelText: 'Peso',
                                    border: OutlineInputBorder(),
                                  ),
                                  keyboardType: TextInputType.number,
                                  onChanged: (value) {
                                    setState(() {
                                      exercicio['peso'] = double.parse(value);
                                    });
                                  },
                                ),
                                SizedBox(height: 20),
                                TextField(
                                  decoration: InputDecoration(
                                    labelText: 'Descanso (segundos)',
                                    border: OutlineInputBorder(),
                                  ),
                                  keyboardType: TextInputType.number,
                                  onChanged: (value) {
                                    setState(() {
                                      exercicio['descanso'] = int.parse(value);
                                    });
                                  },
                                ),
                                SizedBox(height: 20),
                              ],
                            ),
                          ),
                        );
                      }).toList(),
                      ElevatedButton(
                        onPressed: () => addNovoExercicio(dia),
                        child: Text('Adicionar Novo Exercício'),
                      ),
                    ],
                  );
                }).toList(),
              ),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: salvarTodosExercicios,
              child: Text('Salvar Todos os Exercícios'),
            ),
          ],
        ),
      ),
    );
  }
}
                                  
                                    
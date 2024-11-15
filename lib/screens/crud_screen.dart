import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:gym_app/screens/cadastro_paciente_screen.dart'; // Certifique-se de importar corretamente

class CrudScreen extends StatefulWidget {
  final int profissionalId; // Adicione o profissionalId como argumento

  CrudScreen({required this.profissionalId});

  @override
  _CrudScreenState createState() => _CrudScreenState();
}

class _CrudScreenState extends State<CrudScreen> {
  List<Map<String, dynamic>> alunos = [];

  @override
  void initState() {
    super.initState();
    fetchAlunos();
  }

  Future<void> fetchAlunos() async {
    final response = await http.get(Uri.parse('http://10.0.2.2:5000/students/${widget.profissionalId}'));
    if (response.statusCode == 200) {
      setState(() {
        alunos = List<Map<String, dynamic>>.from(json.decode(response.body));
      });
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro ao carregar alunos!')),
      );
    }
  }

  Future<void> deleteAluno(int id) async {
    final response = await http.delete(Uri.parse('http://10.0.2.2:5000/delete_student/$id'));
    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Aluno deletado com sucesso!')),
      );
      fetchAlunos();
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro ao deletar aluno!')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Gerenciar Alunos'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              decoration: InputDecoration(
                labelText: 'Pesquisar Alunos',
                border: OutlineInputBorder(),
              ),
              onChanged: (value) {
                // Implementar lógica de pesquisa se necessário
              },
            ),
            SizedBox(height: 20),
            Expanded(
              child: ListView.builder(
                itemCount: alunos.length,
                itemBuilder: (context, index) {
                  var aluno = alunos[index];
                  return Card(
                    margin: EdgeInsets.symmetric(vertical: 10),
                    child: Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('Nome: ${aluno['nome']}', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                          SizedBox(height: 8),
                          Text('Idade: ${aluno['idade']}'),
                          Text('Peso: ${aluno['peso']} kg'),
                          Text('Altura: ${aluno['altura']} m'),
                          Text('Condições Médicas: ${aluno['condicoes']}'),
                          Text('Lesões: ${aluno['lesoes']}'),
                          Text('Nível de Experiência: ${aluno['experiencia']}'),
                          Text('Frequência de Treino: ${aluno['frequencia']}'),
                          Text('Objetivo: ${aluno['objetivo']}'),
                          Text('Fumante: ${aluno['fumante']}'),
                          Text('Horas de Sono: ${aluno['sono']} por noite'),
                          SizedBox(height: 8),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.end,
                            children: [
                              IconButton(
                                icon: Icon(Icons.edit),
                                onPressed: () {
                                  // Navega para a tela de editar aluno
                                },
                              ),
                              IconButton(
                                icon: Icon(Icons.delete),
                                onPressed: () {
                                  deleteAluno(aluno['id']);
                                },
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  );
                },
              ),
            ),
            ElevatedButton(
              onPressed: () async {
                // Navega para a tela de cadastro de alunos e espera até que o usuário retorne
                await Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => CadastroPacienteScreen(profissionalId: widget.profissionalId)),
                );
                // Recarrega a lista de alunos após o cadastro
                fetchAlunos();
              },
              child: Text('Cadastrar Aluno'),
            ),
          ],
        ),
      ),
    );
  }
}

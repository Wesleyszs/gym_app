import 'package:flutter/material.dart';
import 'package:gym_app/screens/cadastro_paciente_screen.dart'; // Certifique-se de importar corretamente

class CrudScreen extends StatefulWidget {
  @override
  _CrudScreenState createState() => _CrudScreenState();
}

class _CrudScreenState extends State<CrudScreen> {
  // Lista de alunos para exibição
  List<Map<String, String>> alunos = [
    {'nome': 'Wesley', 'idade': '24', 'peso': '85', 'altura': '1.70', 'email': 'wesley@email.com', 'telefone': '8500000000'},
    // Outros alunos...
  ];

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
            ),
            SizedBox(height: 20),
            Expanded(
              child: ListView.builder(
                itemCount: alunos.length,
                itemBuilder: (context, index) {
                  return ListTile(
                    title: Text(alunos[index]['nome']!),
                    subtitle: Text('Idade: ${alunos[index]['idade']} - Email: ${alunos[index]['email']}'),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
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
                            setState(() {
                              alunos.removeAt(index);
                            });
                          },
                        ),
                      ],
                    ),
                    onTap: () {
                      // Navega para a tela de detalhes do aluno
                    },
                  );
                },
              ),
            ),
            ElevatedButton(
              onPressed: () {
                // Navega para a tela de cadastro de alunos
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => CadastroPacienteScreen()),
                );
              },
              child: Text('Cadastrar Aluno'),
            ),
          ],
        ),
      ),
    );
  }
}

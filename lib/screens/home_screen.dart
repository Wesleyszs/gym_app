import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';
import 'package:gym_app/screens/crud_screen.dart'; // Adicione a importação da tela CRUD

class HomeScreen extends StatefulWidget {
  final int profissionalId;

  const HomeScreen({Key? key, required this.profissionalId}) : super(key: key);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;
  final Map<DateTime, bool> _markedDates = {}; // Map to store marked dates

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('CONSULTAS MARCADAS'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TableCalendar(
              firstDay: DateTime.utc(2022, 1, 1),
              lastDay: DateTime.utc(2030, 12, 31),
              focusedDay: _focusedDay,
              selectedDayPredicate: (day) {
                return isSameDay(_selectedDay, day);
              },
              onDaySelected: (selectedDay, focusedDay) {
                setState(() {
                  _selectedDay = selectedDay;
                  _focusedDay = focusedDay;
                  // Toggle the marked state of the selected day
                  if (_markedDates.containsKey(selectedDay)) {
                    _markedDates.remove(selectedDay);
                  } else {
                    _markedDates[selectedDay] = true;
                  }
                });
              },
              calendarBuilders: CalendarBuilders(
                defaultBuilder: (context, day, focusedDay) {
                  if (_markedDates.containsKey(day) && _markedDates[day]!) {
                    return Container(
                      width: 30,
                      height: 30,
                      decoration: const BoxDecoration(
                        color: Colors.red,
                        shape: BoxShape.circle,
                      ),
                      child: Center(
                        child: Text(
                          '${day.day}',
                          style: const TextStyle(color: Colors.white),
                        ),
                      ),
                    );
                  } else {
                    return Center(
                      child: Text(
                        '${day.day}',
                        style: const TextStyle(color: Colors.black),
                      ),
                    );
                  }
                },
              ),
              calendarStyle: const CalendarStyle(
                outsideDaysVisible: false,
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        items: const [
          BottomNavigationBarItem(
            icon: Icon(Icons.home),
            label: 'Home',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.notifications),
            label: 'Notifications',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.person),
            label: 'Profile',
          ),
        ],
        currentIndex: 0, // Mantenha o índice inicial como Home
        onTap: (index) {
          if (index == 2) {
            // Navega para a tela CRUD quando o ícone de perfil é clicado
            Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => CrudScreen(profissionalId: widget.profissionalId)),
            );
          }
        },
      ),
    );
  }
}

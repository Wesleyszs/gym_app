import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;
  Map<DateTime, bool> _markedDates = {}; // Map to store marked dates

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('CONSULTAS MARCADAS'),
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
                markerBuilder: (context, day, events) {
                  if (_markedDates.containsKey(day) && _markedDates[day]!) {
                    return _buildMarker(day, Colors.red, "INDISPONÍVEL");
                  } else if (day.isBefore(DateTime.now())) {
                    return _buildMarker(day, Colors.green, "CONSULTA MARCADA");
                  } else {
                    return _buildMarker(day, Colors.white, "LIVRE");
                  }
                },
              ),
              calendarStyle: CalendarStyle(
                outsideDaysVisible: false,
              ),
            ),
            SizedBox(height: 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                _buildLegend("INDISPONÍVEL", Colors.red),
                _buildLegend("CONSULTA MARCADA", Colors.green),
                _buildLegend("LIVRE", Colors.white),
              ],
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
      ),
    );
  }

  Widget _buildMarker(DateTime day, Color color, String label) {
    return Container(
      width: 30,
      height: 30,
      margin: const EdgeInsets.all(6.0),
      decoration: BoxDecoration(
        color: color,
        shape: BoxShape.circle,
      ),
      child: Center(
        child: Text(
          label,
          style: TextStyle(color: Colors.black, fontSize: 8),
          textAlign: TextAlign.center,
        ),
      ),
    );
  }

  Widget _buildLegend(String label, Color color) {
    return Row(
      children: [
        Container(
          width: 20,
          height: 20,
          decoration: BoxDecoration(
            color: color,
            shape: BoxShape.circle,
          ),
        ),
        SizedBox(width: 8),
        Text(label),
      ],
    );
  }
}

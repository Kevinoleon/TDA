package com.fiuba.galeShapley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

/**
 * Created by marianovazquez on 4/22/17.
 */
public class GaleShapley {

    HashMap<Integer, ArrayList<Integer>> listaDePreferenciasPretendientes;
    HashMap<Integer, ArrayList<Integer>> listaDePreferenciasOferentes;
    HashMap<Integer, Integer> parejas;

    public GaleShapley(HashMap<Integer, ArrayList<Integer>> listaDePreferenciasPretendientes, HashMap<Integer, ArrayList<Integer>> listaDePreferenciasOferentes) {
        this.listaDePreferenciasPretendientes = listaDePreferenciasPretendientes;
        this.listaDePreferenciasOferentes = listaDePreferenciasOferentes;
        this.parejas = new HashMap<>();
        this.calcularParejas();
    }

    public HashMap<Integer, Integer> getParejas()  {

        return this.parejas;
    }

    private void calcularParejas() {

        Stack<Integer> pretendientesLibres = new Stack<>();
        pretendientesLibres.addAll(listaDePreferenciasPretendientes.keySet());

        HashMap<Integer, Stack<Integer>> pretendientesPosiblesOferentes = new HashMap<>();
        for (int i = 0; i < listaDePreferenciasPretendientes.size(); i++) {
            ArrayList<Integer> preferenciaPretendiente = listaDePreferenciasPretendientes.get(i);
            Stack<Integer> posiblesOferentes = new Stack<>();
            posiblesOferentes.addAll(preferenciaPretendiente);
            pretendientesPosiblesOferentes.put(i, posiblesOferentes);
        }

        Integer[] actualPretendienteDeOferentes = new Integer[listaDePreferenciasOferentes.size()];

        // Dado que el escenario es cuadrado (n pretendientes, n oferentes)
        // y todos los pretendientes tienen a las oferentes en su lista de preferencia (y viceversa)
        // puedo asumir que no se va a dar el caso donde un pretendiente quede libre aún luego de proponersele a todos los oferentes
        while (!pretendientesLibres.empty()) {

            int pretendiente = pretendientesLibres.pop();
            int oferente = pretendientesPosiblesOferentes.get(pretendiente).pop();

            // Chequeo si el oferente en pareja con alguien
            Integer actualPretendienteDeOferente = actualPretendienteDeOferentes[oferente];

            if (actualPretendienteDeOferente == null) {

                // No está en pareja. Agrego la pareja de oferente-pretendiente
                parejas.put(pretendiente, oferente);
                actualPretendienteDeOferentes[oferente] = pretendiente;
            } else {

                // Chequeo si el pretendiente es mejor pareja que la actual
                ArrayList<Integer> preferenciasOferente = this.listaDePreferenciasOferentes.get(oferente);
                boolean esMejorPretendienteQueOferente = this.esMejorPretendienteQueOferente(preferenciasOferente, pretendiente, actualPretendienteDeOferente);

                if (esMejorPretendienteQueOferente) {

                    // Es mejor, libero al pretendiente anterior
                    parejas.remove(actualPretendienteDeOferente);
                    pretendientesLibres.push(actualPretendienteDeOferente);

                    // Y agrego la pareja de oferente-pretendiente
                    parejas.put(pretendiente, oferente);
                    actualPretendienteDeOferentes[oferente] = pretendiente;
                } else {
                    // No encontré pareja, el pretendiente sigue libre
                    pretendientesLibres.push(pretendiente);
                }
            }
        }
    }

    // Chequeo si el pretendiente es mejor pareja que la actual al revisar en
    // la lista de preferencias del oferente quién está primero
    private boolean esMejorPretendienteQueOferente(ArrayList<Integer> preferenciasOferente, int pretendiente, int actualPretendienteDeOferente) {

        boolean esMejorPretendienteQueOferente = false;
        for (int i = 0; i < preferenciasOferente.size(); i++) {
            if (preferenciasOferente.get(i) == pretendiente) {
                esMejorPretendienteQueOferente = true;
                break;
            }
            if (preferenciasOferente.get(i) == actualPretendienteDeOferente) {
                break;
            }
        }

        return esMejorPretendienteQueOferente;
    }
}

package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  // En el contructor Cuenta() no se puede instanciar de dos maneras distintas "saldo", el tipo de code smell que identifico aca es el de Duplicated Code
  // porque se repite logica. Se escriben de dos maneras la misma idea de codigo, cuando el saldo podria considerarse como 0 o que sea una variable generica.
   /* public Cuenta() {
    saldo = 0;
  }
  */

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  // En el caso de setMovimientos(List<Movimiento> movimientos), considero que habria que analizar si es necesario que este este setter ya que si no
  // se utiliza para otras clases, no es necesario que se cree.
    public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }


  // En el metodo "poner(double cuanto)" lo considero como un Misplaced method porque no esta utilizando ninguna variable de la clase "Cuenta" ni esta
  // utilizando comportamiento interno. Tranquilamente podria ir directo en la clase Movimiento.
  // Tambien considere que podria ser un tipo de code smell Feature Envy, ya que el metodo poner() le evia demasiados mensajes
  // a la clase Movimiento. Podria ser directamente la responsabilidad del Movimiento enviarle la informacion que le pide
  // delegando. --> No estaba segura si el Feature Envy esta bien considerado pero lo agregue por las dudas.

  public void poner(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException("El monto a ingresar debe ser un valor positivo");
    }
     if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
  }

  public void sacar(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    // En este caso tambien se puede considerar como un tipo de code smell Feature Envy porque
    // la informacion que se busca en getMontoExtraidoA() se la podria enviar directamente la clase Movimiento en un submetodo.
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    // Se podria encontrar en este caso tambien un code smell del tipo Message Chains ya que todo lo que devuelve ese return
    // se podria delegar en otro metodo.
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

   public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  // Aca hay tambien Duplicate code como estaba arriba. Usar un constructor es casi lo mismo que el setter. Si ya tenemos
  // el constructor, no es necesario que usemos el setter.
  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}

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
  // private List<Movimiento> movimientos = new ArrayList<>();
  // Al haber hecho una herencia, considere mas facil que la cuenta conozca dos listas que sea una de las extraccion y otra de los depositos que hizo
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extracciones = new ArrayList<>();


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
 /*   public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }
  AL HABER SACADO ESTA LISTA, NO SIRVE MAS ESTA FUNCION
  */

  // En el caso de poner y sacar hay Duplicate code porque hacen cosas parecidas. Dentro de ambas se podria delegar la primer condicion, de esta forma no se repite tanto la logica.
  // Ademas faltaria modificar el saldo de la cuenta cada vez que se saca y pone plata en la cuenta.
  public void validarSiEsMontoNegativo(double cuanto){
    if (cuanto <= 0) {
      throw new MontoNegativoException("El monto a ingresar debe ser un valor positivo");
    }
  }

  public void poner(double cuanto) {
    validarSiEsMontoNegativo(cuanto);

    // Hay Message Chains dentro de la condicion del if, podria delegarse
     if (depositos.size() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
     this.saldo = saldo + cuanto;
    Movimiento nuevoMovimiento = new Movimiento(LocalDate.now(), cuanto, true);
    this.agregarMovimiento(nuevoMovimiento);
  }

  public void sacar(double cuanto) {
    validarSiEsMontoNegativo(cuanto);

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

    this.saldo = saldo - cuanto;
    Movimiento nuevoMovimiento = new Movimiento(LocalDate.now(), cuanto, false);
    this.agregarMovimiento(nuevoMovimiento);
  }

  public void agregarMovimiento(Movimiento movimiento) {
    // Es mas sencillo llamar directamente al movimiento que ya se creo
    // Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
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
  /*public void setSaldo(double saldo) {
    this.saldo = saldo;
  }
*/
}

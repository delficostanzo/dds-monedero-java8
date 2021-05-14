package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
// Al considerarse un monto inicial, lo mejor seria que arranque desde 0.
    cuenta = new Cuenta(0);
  }

  @Test
  void PonerSaldoNuevoEnLaCuenta() {
    cuenta.poner(1500);
// En este caso, agrego un assert para verificar que la funcion poner funciona bien
    assertEquals(cuenta.getSaldo(),1500);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositosEstanPermitidos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getSaldo(),1500+456+1900);
// Al no tirar ninguna excepcion, esta bien agregar 3 depositos.
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(456);
      cuenta.poner(1900);
      cuenta.poner(245);
    });
  }


  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
      //aca decidimos que lo indicado no era setear ya que sino no respetariamos lo del encapsulamiento,
      // ademas para agregar saldo, lo indicado seria agregarselo en el constructor o al realizar un deposito
      Cuenta nueva = cuentaAgregada(90);
      nueva.sacar(100);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      Cuenta nueva = cuentaAgregada(5000);
      nueva.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

// con cuentaAgregada() se puede crear una cuenta nueva sin tener que andar setteando cada vez que corremos un test
  Cuenta cuentaAgregada(double saldo){
    return new Cuenta(saldo);
  }

  // TESTS AGREGADOS
  @Test
  public void RealizarDosDepositosYUnoNegativo(){
    // En el caso de este test, luego de agregar dos depositos nuevos, intena agregar uno negativo que no se puede.
    assertThrows(MontoNegativoException.class, () -> {
      cuenta.poner(100);
      cuenta.poner(1500);
      cuenta.poner(-500);
    });
  }

  @Test
  public void AgregarTresDepositosYUnoNegativo(){
    // En este caso, como hay 2 excepciones, la primera que va a analizar es la del monto negativo porque la excepciones se analizan por orden.
    assertThrows(MontoNegativoException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(2000);
      cuenta.poner(500);
      cuenta.poner(-500);
    });
  }


  @Test
  public void PonerSaldoNuevoYExtraerMasDeMil(){
    //En este caso, al ser la primera vez que deposita plata, se puede extraer mas de mil
    assertThrows(SaldoMenorException.class, () -> {
      Cuenta cuentaNueva = cuentaAgregada(2000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerDosVecesYQueLaSegundaSeaMasQueElSaldo(){
    // En este caso, analiza la excepcion de que esta sacando mas saldo de lo que tiene en su cuenta.
    assertThrows(SaldoMenorException.class, () -> {
       Cuenta nueva = cuentaAgregada(1000);
        nueva.sacar(100);
        nueva.sacar(1500);
    });
  }
}




package es.bsc.demiurge.renewit.modellers;

import es.bsc.demiurge.core.clopla.domain.Host;
import es.bsc.demiurge.core.clopla.domain.Vm;
import es.bsc.demiurge.core.drivers.Estimator;
import es.bsc.demiurge.core.models.scheduling.DeploymentPlan;
import es.bsc.demiurge.core.models.scheduling.VmAssignmentToHost;
import es.bsc.demiurge.core.models.vms.VmDeployed;
import es.bsc.demiurge.renewit.manager.PerformanceVmManager;

import java.util.List;
import java.util.Map;

/**
 * Dummy energy modeller. To be implemented by Mauro's model
 *
 * @author Mario Macias (http://github.com/mariomac), Mauro Canuto (mauro.canuto@bsc.es)
 */
public class PowerModeller implements Estimator {

	private double DUMMY_POWER_PER_CPU_CORE = 30;
	private double DUMMY_POWER_PER_IDLE_HOST = 50;

	@Override
	public String getLabel() {
		return "power";
	}

	/**
	 * By the moment, calculates dummy power
	 * @param vma
	 * @param vmsDeployed
	 * @param deploymentPlan THIS IS BY THE MOMENT IGNORED. I THINK IT IS USED ONLY BY ASCETIC
	 * @return
	 */
	// TODO: check if deploymenplan is ascetic-specific, and must be removed from the core
	// TODO: mix getDeploymentEstimation and getCloplaEstimation
	@Override
	public double getDeploymentEstimation(VmAssignmentToHost vma, List<VmDeployed> vmsDeployed, DeploymentPlan deploymentPlan) {
		double pow = DUMMY_POWER_PER_IDLE_HOST + vma.getVm().getCpus() * DUMMY_POWER_PER_CPU_CORE;
		for(VmDeployed vm : vmsDeployed) {
			pow += vm.getCpus() * DUMMY_POWER_PER_CPU_CORE;
		}
		return pow;
	}

	@Override
	public double getCurrentEstimation(String vmId, Map options) {
		throw new AssertionError("this should be never called. VMM is configured for Clopla and must get rid of legacy scheduler");
	}

	@Override
	public double getCloplaEstimation(Host host, List<Vm> vmsDeployedInHost) {
		double pow = DUMMY_POWER_PER_IDLE_HOST;
		for(Vm vm : vmsDeployedInHost) {
			pow += vm.getNcpus() * DUMMY_POWER_PER_CPU_CORE;
		}
		return pow;
	}

	/**
	 *
	 * This method return the power consumption of the host. Actual consumption can be calculate in 2 ways:
	 *  - Directly reading the power meter of the host and summing the non-deployed-VMs power estimation.
	 *  - If the host does not have a power meter summing the power estimation of all the VMs
	 *
	 * @param host
	 * @param vmsDeployedInHost
	 * @return
	 */
	public double getCloplaHostPowerConsumptionPerformanceModels(Host host, List<Vm> vmsDeployedInHost) {
		// 5 as overhead of virtualization: to change
		double pow = 0;

		// Sum idle of host if on  //&& vmsDeployedInHost.size() == 0
		if (!(host.wasOffInitiallly())){
			pow +=host.getIdlePower();
		}

		if (vmsDeployedInHost.size() > 0){
			// Power of VMs already deployed + power estimation of VM to be deployed
			for(Vm vm : vmsDeployedInHost) {
				pow += vm.getPowerEstimation() - host.getIdlePower();
			}


			// 5 as overhead of virtualization: to change
			//pow += 5;
		}

		return pow;
	}
	public double getCloplaHostPowerConsumptionPerformanceModelsJuno(Host host, List<Vm> vmsDeployedInHost, PerformanceVmManager solution) {
		// 5 as overhead of virtualization: to change
		double pow = 0;

		// Sum idle of host if on  //&& vmsDeployedInHost.size() == 0
		/*if (!(host.wasOffInitiallly())){
			pow += host.getIdlePower();
		}*/

		if (vmsDeployedInHost.size() > 0){
			if (checkJunoSamePlatform(host.getHostname(), solution)){
				pow += host.getIdlePower()/2;
			}else{
				pow += host.getIdlePower();
			}

			// Power of VMs already deployed + power estimation of VM to be deployed
			for(Vm vm : vmsDeployedInHost) {
				pow += vm.getPowerEstimation() - host.getIdlePower();
			}



			// 5 as overhead of virtualization: to change
			//pow += 5;
		}

		return pow;
	}
	private boolean checkJunoSamePlatform(String hostname, PerformanceVmManager solution) {

		if (hostname.equals("host0")){
			if (solution.getHost("host1").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host2")){
			if (solution.getHost("host3").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host4")){
			if (solution.getHost("host5").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host6")){
			if (solution.getHost("host7").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host8")){
			if (solution.getHost("host9").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host10")){
			if (solution.getHost("host11").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host12")){
			if (solution.getHost("host13").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host14")){
			if (solution.getHost("host15").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host16")){
			if (solution.getHost("host17").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host18")){
			if (solution.getHost("host19").getAssignedCpus() > 0)
				return true;
			else return false;
		}

		else if (hostname.equals("host19")){
			if (solution.getHost("host18").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host17")){
			if (solution.getHost("host16").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host15")){
			if (solution.getHost("host14").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host13")){
			if (solution.getHost("host12").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host11")){
			if (solution.getHost("host10").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host9")){
			if (solution.getHost("host8").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host7")){
			if (solution.getHost("host6").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host5")){
			if (solution.getHost("host4").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host3")){
			if (solution.getHost("host2").getAssignedCpus() > 0)
				return true;
			else return false;
		}else if (hostname.equals("host1")){
			if (solution.getHost("host0").getAssignedCpus() > 0)
				return true;
			else return false;
		}else
			return false;
	}

}

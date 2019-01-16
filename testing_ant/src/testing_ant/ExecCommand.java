package testing_ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecCommand {
	private Process process;
	private String command;
	private File commandDir;
	private File outLog;
	private File errLog;

	/**
	 * @author dandredd
	 *
	 */
	private class OutputReader implements Callable<String> {

		private File outLog;

		public OutputReader(File outLog) {
			this.outLog = outLog;
		}

		public String call() {
			PrintWriter out = null;
			try {
				out = new PrintWriter(outLog);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					out.println(line);
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				closeResource(out);
			}
			return null;
		}
	}

	private class ErrorReader implements Callable<String> {

		private File errLog;
		private boolean errorFlag = false;

		public ErrorReader(File errLog) {
			this.errLog = errLog;
		}

		public String call() {
			PrintWriter err = null;
			try {
				err = new PrintWriter(errLog);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					err.println(line);
					if (!line.trim().isEmpty()) {
						errorFlag = true;
					}
					err.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				closeResource(err);
			}
			return errorFlag ? "Error" : null;
		}
	}

	public ExecCommand(String command, File commandDir, File outLog, File errLog) {
		this.command = command;
		this.commandDir = commandDir;
		this.outLog = outLog;
		this.errLog = errLog;
	}

	public void execute() {
		try {
			process = Runtime.getRuntime().exec(command, null, commandDir);

			ExecutorService threadPool = Executors.newFixedThreadPool(2);
			// Future<String> inputFuture, outputFuture, errorFuture;
			Future<String> outStatus = threadPool.submit(new OutputReader(outLog));
			Future<String> errStatus = threadPool.submit(new ErrorReader(errLog));
			// wait till command completes
			process.waitFor();
			// Wait for out/error reader to complete logging and shutdown service
			outStatus.get();
			errStatus.get();
			threadPool.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	public static void closeResource(AutoCloseable ps) {
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}